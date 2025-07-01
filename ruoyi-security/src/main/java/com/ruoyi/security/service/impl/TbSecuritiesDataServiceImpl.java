package com.ruoyi.security.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.Constant;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.security.algorithm.CoreAlgorithmContet;
import com.ruoyi.security.domain.TbFluctuationLog;
import com.ruoyi.security.domain.TbSecuritiesHistory;
import com.ruoyi.security.domain.TbSinaFifteen;
import com.ruoyi.security.service.ITbFluctuationLogService;
import com.ruoyi.security.service.ITbSecuritiesHistoryService;
import com.ruoyi.security.service.ITbSinaFifteenService;
import com.ruoyi.security.task.TbSecuritiesDataSinaThread;
import com.ruoyi.security.task.TbSecuritiesDataThread;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import com.ruoyi.security.vo.SecuritiesSinaFutureVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import com.ruoyi.security.mapper.TbSecuritiesDataMapper;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.service.ITbSecuritiesDataService;
import org.springframework.util.CollectionUtils;

/**
 * 证劵交易Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
@Service
@Slf4j
public class TbSecuritiesDataServiceImpl implements ITbSecuritiesDataService 
{
    @Autowired
    private TbSecuritiesDataMapper tbSecuritiesDataMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private CoreAlgorithmContet coreAlgorithmContet;

    @Autowired
    private ITbFluctuationLogService iTbFluctuationLogService;

    @Autowired
    private ITbSecuritiesHistoryService iTbSecuritiesHistoryService;

    @Autowired
    private ITbSinaFifteenService iTbSinaFifteenService;

    ReentrantLock lock = new ReentrantLock();

    /**
     * 查询证劵交易
     * 
     * @param id 证劵交易主键
     * @return 证劵交易
     */
    @Override
    public TbSecuritiesData selectTbSecuritiesDataById(Long id)
    {
        return tbSecuritiesDataMapper.selectTbSecuritiesDataById(id);
    }

    /**
     * 查询证劵交易列表
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 证劵交易
     */
    @Override
    public List<TbSecuritiesData> selectTbSecuritiesDataList(TbSecuritiesData tbSecuritiesData)
    {
        return tbSecuritiesDataMapper.selectTbSecuritiesDataList(tbSecuritiesData);
    }

    /**
     * 新增证劵交易
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 结果
     */
    @Override
    public int insertTbSecuritiesData(TbSecuritiesData tbSecuritiesData)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        redisCache.deleteObject("tbSecuritiesSinaDataList");
        return tbSecuritiesDataMapper.insertTbSecuritiesData(tbSecuritiesData);
    }

    /**
     * 修改证劵交易
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 结果
     */
    @Override
    public int updateTbSecuritiesData(TbSecuritiesData tbSecuritiesData)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        redisCache.deleteObject("tbSecuritiesSinaDataList");
        return tbSecuritiesDataMapper.updateTbSecuritiesData(tbSecuritiesData);
    }

    /**
     * 批量删除证劵交易
     * 
     * @param ids 需要删除的证劵交易主键
     * @return 结果
     */
    @Override
    public int deleteTbSecuritiesDataByIds(Long[] ids)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        redisCache.deleteObject("tbSecuritiesSinaDataList");
        return tbSecuritiesDataMapper.deleteTbSecuritiesDataByIds(ids);
    }

    /**
     * 删除证劵交易信息
     * 
     * @param id 证劵交易主键
     * @return 结果
     */
    @Override
    public int deleteTbSecuritiesDataById(Long id)
    {
        redisCache.deleteObject("tbSecuritiesDataList");
        redisCache.deleteObject("tbSecuritiesSinaDataList");
        return tbSecuritiesDataMapper.deleteTbSecuritiesDataById(id);
    }

    /**
     * 爬取证劵交易数据
     * @return boolean
     */
    @Override
    public boolean crawl() {
        //1、爬东方财富数据
        List<String> listF = Arrays.asList("103", "112", "113", "114", "115");
        //List<String> listF = Arrays.asList("115");
        LinkedList<TbSecuritiesData> tbSecuritiesDataLinkedList = new LinkedList<>();
        listF.forEach(s -> {
            Map urlMap = new HashMap<>();
            urlMap.put("place", s);
            //发送http请求
            String rx = null;
            try {
                rx = HttpUtils.sendGet(new StrSubstitutor(urlMap).replace(Constant.FUTURESMAINFORCEURL).toString());
            } catch (Exception e) {
                log.error("爬取数据异常：",e);
            }
            Map node = (Map) JSON.parse(rx);
            List<Map<String, Object>> list = (List<Map<String, Object>>) node.get("list");
            //数据集
            System.out.println(list.toArray());
            list.forEach(map -> {
                String name = (String) map.get("name");
                TbSecuritiesData tbSecuritiesData = new TbSecuritiesData();
//                if ((name.contains("主") && !name.contains("次")) || name.contains("250") || name.contains("碱")) {
//                    TbSecuritiesData tbSecuritiesData = new TbSecuritiesData();
//                    if ("103".equals(s)) {
//                        name = new StringBuilder(name).append("(美)").toString();
//                    }
                    tbSecuritiesData.setName(name);
                    String dm = (String) map.get("dm");
                    tbSecuritiesData.setCode(dm);
                    tbSecuritiesData.setExchangeCode(new StringBuilder(s).append(".").append(dm).toString());
                    tbSecuritiesData.setDeviation(90.0);
                    tbSecuritiesData.setType(1);
                    tbSecuritiesData.setStatus(1);
                    tbSecuritiesData.setAddUser("admin");
                    tbSecuritiesData.setAddTime(new Date());
                    tbSecuritiesDataLinkedList.add(tbSecuritiesData);
//                }
            });
        });
        if (!CollectionUtils.isEmpty(tbSecuritiesDataLinkedList)){
            //只删除东方财富数据
            tbSecuritiesDataMapper.deleteAll();
            tbSecuritiesDataMapper.insertList(tbSecuritiesDataLinkedList);
        }
        return true;
    }

    @Override
    public List<SecuritiesFutureVo> lists() {
        //1.查询有效配置
        List<SecuritiesFutureVo> securitiesFutureVoList = redisCache.getCacheMapValue("money","securitiesFutureVoList");
        return CollectionUtils.isEmpty(securitiesFutureVoList)?new ArrayList<>():securitiesFutureVoList;
    }

    @Override
    public List<SecuritiesFutureVo> findList() {
        //1.查询有效配置
        List<TbSecuritiesData> tbSecuritiesDataList = redisCache.getCacheMapValue("money","f_dongfang_tbSecuritiesDataList");
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return new ArrayList<>();
        }
        tbSecuritiesDataList = tbSecuritiesDataList.stream().filter(t -> t.getStatus() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return new ArrayList<>();
        }
        List<Future<SecuritiesFutureVo>> futures = new ArrayList<>();
        //2、构建线程
        long startTime = System.currentTimeMillis();
        for (TbSecuritiesData tbSecuritiesData : tbSecuritiesDataList) {
            TbSecuritiesDataThread tbSecuritiesDataThread = new TbSecuritiesDataThread(tbSecuritiesData, coreAlgorithmContet);
            Future<SecuritiesFutureVo> future = taskExecutor.submit(tbSecuritiesDataThread);
            futures.add(future);
        }
        //3、等待所有任务完成
        List<SecuritiesFutureVo> list = new ArrayList<>();
        for (Future<SecuritiesFutureVo> future : futures) {
            try {
                list.add(future.get());// 阻塞直到任务完成
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 自定义降序排序
        Collections.sort(list, new Comparator<SecuritiesFutureVo>() {
            @Override
            public int compare(SecuritiesFutureVo o1, SecuritiesFutureVo o2) {
                return o2.getPositiveNegativeFlag().compareTo(o1.getPositiveNegativeFlag()); // 降序排序
            }
        });
        long endTime = System.currentTimeMillis();
        log.info("执行时长：{}", endTime - startTime);
        return list;
    }

    @Override
    public List<SecuritiesSinaFutureVo> findSinaList() {
        //1.查询有效配置

        List<TbSecuritiesData> tbSecuritiesSinaDataList = redisCache.getCacheMapValue("money","f_sina_tbSecuritiesDataList");
        if (CollectionUtils.isEmpty(tbSecuritiesSinaDataList)){
            return new ArrayList<>();
        }
        tbSecuritiesSinaDataList = tbSecuritiesSinaDataList.stream().filter(t -> t.getStatus() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tbSecuritiesSinaDataList)){
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(tbSecuritiesSinaDataList)){
            return new ArrayList<>();
        }
        List<Future<SecuritiesSinaFutureVo>> futures = new ArrayList<>();
        //2、构建线程
        long startTime = System.currentTimeMillis();
        for (TbSecuritiesData tbSecuritiesData : tbSecuritiesSinaDataList) {
            //获取15分钟上中下
            TbSecuritiesDataSinaThread tbSecuritiesDataThread = new TbSecuritiesDataSinaThread(tbSecuritiesData, coreAlgorithmContet, Constant.SINA_FIFTEEN_MIN_LINE);
            Future<SecuritiesSinaFutureVo> future = taskExecutor.submit(tbSecuritiesDataThread);
            futures.add(future);
        }
        //3、等待所有任务完成
        List<SecuritiesSinaFutureVo> list = new ArrayList<>();
        for (Future<SecuritiesSinaFutureVo> future : futures) {
            try {
                list.add(future.get());// 阻塞直到任务完成
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 自定义升序排序
        Collections.sort(list, new Comparator<SecuritiesSinaFutureVo>() {
            @Override
            public int compare(SecuritiesSinaFutureVo o1, SecuritiesSinaFutureVo o2) {
                return o1.getPositiveNegativeFlag().compareTo(o2.getPositiveNegativeFlag()); // 降序排序
            }
        });
        long endTime = System.currentTimeMillis();
        log.info("执行时长：{}", endTime - startTime);
        return list;
    }

     @Override
    public void updateFluctuationLog() {
        try {
            TbSecuritiesData tbSecuritiesDataQeury = new TbSecuritiesData();
            tbSecuritiesDataQeury.setType(2);
            tbSecuritiesDataQeury.setName("连续");
            List<TbSecuritiesData> tbSecuritiesSinaDataList = tbSecuritiesDataMapper.selectTbSecuritiesDataList(tbSecuritiesDataQeury);
            if (CollectionUtils.isEmpty(tbSecuritiesSinaDataList)){
                return;
            }
            iTbFluctuationLogService.delateAllByLogType(1);
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            // 定义格式化模式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M_d");
            // 格式化日期
            String formattedDate = currentDate.format(formatter);
            Map urlMap = new HashMap<>();
            DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (TbSecuritiesData tbSecuritiesData:tbSecuritiesSinaDataList){
                List<TbFluctuationLog> list = new ArrayList<>();
                urlMap.clear();
                urlMap.put("variety1", new StringBuilder(tbSecuritiesData.getCode()).append("_").append(formattedDate).toString());
                urlMap.put("variety2", tbSecuritiesData.getCode());
                HttpGet httpGet = new HttpGet(new StrSubstitutor(urlMap).replace(Constant.SINA_DATE_DATA));
                //发送http请求
                String rx = HttpUtils.sendGet(new StrSubstitutor(urlMap).replace(Constant.SINA_DATE_DATA));
                String str = rx.substring(rx.indexOf("(") + 1, rx.lastIndexOf(")"));
                JSONArray jsonArray = JSONArray.parse(str);
                for (int i = jsonArray.size() - 1; i >= jsonArray.size() - 20; i--) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    TbFluctuationLog tbFluctuationLog = new TbFluctuationLog();
                    tbFluctuationLog.setCode(tbSecuritiesData.getCode());
                    tbFluctuationLog.setName(tbSecuritiesData.getName());
                    tbFluctuationLog.setLogType(1);
                    String h = (String) jsonObject.get("h");
                    String l = (String) jsonObject.get("l");
                    String d = (String) jsonObject.get("d");
                    LocalDate localDate = LocalDate.parse(d, formatterD);
                    tbFluctuationLog.setLogDate(Date.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
                    Double price = Double.valueOf(h) - Double.valueOf(l);
                    tbFluctuationLog.setUndulate(price);
                    list.add(tbFluctuationLog);
                }
                iTbFluctuationLogService.insertTbFluctuationLogList(list);
                Thread.sleep(10000);
            }
        }catch (Exception e){
            log.error("同步异常");
        }

    }

    @Override
    public List<SecuritiesSinaFutureVo> findSinaFiveList() {
        List<SecuritiesSinaFutureVo> securitiesSinaListVoList = redisCache.getCacheMapValue("money","securitiesSinaListVo");
        if (CollectionUtils.isEmpty(securitiesSinaListVoList)){
            return new ArrayList<>();
        }
        return securitiesSinaListVoList.stream().filter(s->s.getPositiveNegativeFlag() ==1 || s.getPositiveNegativeFlag() ==2 || s.getPositiveNegativeFlag() ==3 || s.getPositiveNegativeFlag() ==4).collect(Collectors.toList());
    }

    @Override
    public void synSinaFiveData() {
        try {
            TbSecuritiesData tbSecuritiesDataEntity = new TbSecuritiesData();
            tbSecuritiesDataEntity.setType(2);
            List<TbSecuritiesData> tbSecuritiesSinaDataList = tbSecuritiesDataMapper.selectTbSecuritiesDataList(tbSecuritiesDataEntity);
            if (CollectionUtils.isEmpty(tbSecuritiesSinaDataList)){
                return;
            }
            for (TbSecuritiesData tbSecuritiesData : tbSecuritiesSinaDataList) {
                //获取15分钟上中下
                TbSecuritiesDataSinaThread tbSecuritiesDataThread = new TbSecuritiesDataSinaThread(tbSecuritiesData, coreAlgorithmContet, Constant.SINA_FIFTEEN_MIN_LINE);
                SecuritiesSinaFutureVo securitiesSinaFutureVo = tbSecuritiesDataThread.call();
                List<SecuritiesSinaFutureVo> securitiesSinaListVoList = null;
                if (CollectionUtils.isEmpty(securitiesSinaListVoList)){
                    securitiesSinaListVoList = new ArrayList<>();
                    securitiesSinaListVoList.add(securitiesSinaFutureVo);
                }else {
                    securitiesSinaListVoList = redisCache.getCacheMapValue("money","securitiesSinaListVo");
                    SecuritiesSinaFutureVo removeSecuritiesSinaFutureVo = null;
                    for (SecuritiesSinaFutureVo securitiesSinaFuture:securitiesSinaListVoList){
                        if (securitiesSinaFuture.getCode().equals(securitiesSinaFutureVo.getCode())){
                            removeSecuritiesSinaFutureVo = securitiesSinaFuture;
                            break;
                        }
                    }
                    if (null != removeSecuritiesSinaFutureVo){
                        securitiesSinaListVoList.remove(removeSecuritiesSinaFutureVo);
                    }
                    securitiesSinaListVoList.add(securitiesSinaFutureVo);
                }
                redisCache.setCacheMapValue("money","securitiesSinaListVo",securitiesSinaListVoList);
                Thread.sleep(10000);
            }
        }catch (Exception e){
            log.error("同步异常");
        }
    }

    @Override
    public List<SecuritiesSinaFutureVo> findDongFangFiveList() throws Exception {
        //1.查询有效配置
        List<TbSecuritiesData> tbSecuritiesDataList = redisCache.getCacheMapValue("money","f_dongfang_tbSecuritiesDataList");
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return new ArrayList<>();
        }
        tbSecuritiesDataList = tbSecuritiesDataList.stream().filter(t -> t.getStatus() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return new ArrayList<>();
        }
        List<TbSecuritiesHistory> list = new ArrayList<>();
        for (TbSecuritiesData tbSecuritiesData:tbSecuritiesDataList){
            //拼接地址
            Map urlMap = new HashMap<>();
            urlMap.put("futuresUrl", tbSecuritiesData.getExchangeCode());
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(new StrSubstitutor(urlMap).replace(Constant.futuresUrl));
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");
            httpGet.setHeader("Referer", new StrSubstitutor(urlMap).replace(Constant.REFERER));
            httpGet.setHeader("Accept", "*/*");
            httpGet.setHeader(":authority", "stock2.finance.sina.com.cn");
            httpGet.setHeader("cookie", "UOR=www.baidu.com,finance.sina.com.cn,; SFA_version8.8.0=2025-03-04%2022%3A03; SINAGLOBAL=117.61.111.242_1741097176.634996; U_TRS1=000000f2.89225770.67c708dd.ad19008d; SFA_version8.9.0=2025-03-15%2013%3A54; Apache=117.61.85.101_1742018202.401408; SFA_version8.9.0_click=1; hqEtagMode=1; rotatecount=2; ULV=1742018211635:4:4:2:117.61.85.101_1742018202.401408:1742018199525; U_TRS2=00000065.45e175a.67d516a6.48bc3ee3; FIN_ALL_VISITED=HC0%2CMA0%2CTA0%2CV0%2CFU0%2CLC2602%2CRB0%2CCF2601%2CRM2505%2CRB2505%2CCY0; NEWESTVISITED_FUTURE=%7B%22code%22%3A%22HC0%22%2C%22hqcode%22%3A%22nf_HC0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22MA0%22%2C%22hqcode%22%3A%22nf_MA0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22TA0%22%2C%22hqcode%22%3A%22nf_TA0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22V0%22%2C%22hqcode%22%3A%22nf_V0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22FU0%22%2C%22hqcode%22%3A%22nf_FU0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22LC2602%22%2C%22hqcode%22%3A%22nf_LC2602%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22RB0%22%2C%22hqcode%22%3A%22nf_RB0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22CF2601%22%2C%22hqcode%22%3A%22nf_CF2601%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22RM2505%22%2C%22hqcode%22%3A%22nf_RM2505%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22RB2505%22%2C%22hqcode%22%3A%22nf_RB2505%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22CY0%22%2C%22hqcode%22%3A%22nf_CY0%22%2C%22type%22%3A1%7D");
            //发送http请求
            HttpResponse response = httpClient.execute(httpGet);
            String rx = EntityUtils.toString(response.getEntity());
            Map node = null;
            Map map = null;
            List trendsList = null;
            try {
                node = (Map) JSON.parse(rx);
                map = (Map) node.get("data");
                trendsList = (List) map.get("trends");
            }catch (Exception e){
                continue;
            }
            int num = 1;
            int step = 15;
            List<Double> priceList = new ArrayList<>();
            while (num*step-1<=trendsList.size()){
                String s = (String) trendsList.get(num*step-1);
                String[] split = s.split(",");
                double s1 = Double.valueOf(split[1]);
                priceList.add(s1);
                num++;
            }
            TbSecuritiesHistory tbSecuritiesHistory = new TbSecuritiesHistory();
            tbSecuritiesHistory.setCode(tbSecuritiesData.getCode());
            tbSecuritiesHistory.setName(tbSecuritiesData.getName());
            tbSecuritiesHistory.setData(JSONObject.toJSONString(priceList));
            tbSecuritiesHistory.setLogType(1);
            tbSecuritiesHistory.setLogDate(new Date());
            list.add(tbSecuritiesHistory);
            Thread.sleep(3000);
        }
        iTbSecuritiesHistoryService.insertTbSecuritiesHistoryList(list);
        return null;
    }

    @Override
    public void logSina15() throws InterruptedException {
        log.debug("=====================开始执行logSina15=====================");
        boolean b = lock.tryLock();
        if (!b){
            return;
        }
        try {
            // 获取当前时间
            LocalTime now = LocalTime.now();
            //判断是否是周末
            DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
                return;
            }
            // 定义目标时间 11:35
            LocalTime targetTime1 = LocalTime.of(9, 00);
            LocalTime targetTime2 = LocalTime.of(11, 31);
            LocalTime targetTime3 = LocalTime.of(13, 30);
            LocalTime targetTime4 = LocalTime.of(15, 01);
            LocalTime targetTime5 = LocalTime.of(21, 00);
            LocalTime targetTime6 = LocalTime.of(23, 00);
            if ((now.isAfter(targetTime1) && now.isBefore(targetTime2)) || (now.isAfter(targetTime3) && now.isBefore(targetTime4))
                    || (now.isAfter(targetTime5) && now.isBefore(targetTime6))){
                List<TbSecuritiesData> tbSecuritiesDataList = redisCache.getCacheMapValue("money","f_sina_tbSecuritiesDataList");
                if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
                    return;
                }
                long startTime = System.currentTimeMillis();
                for (TbSecuritiesData tbSecuritiesData : tbSecuritiesDataList) {
                    TbSecuritiesDataSinaThread tbSecuritiesDataThread = new TbSecuritiesDataSinaThread(tbSecuritiesData, coreAlgorithmContet, Constant.SINA_FIFTEEN_MIN_LINE);
                    SecuritiesSinaFutureVo securitiesSinaFutureVo = tbSecuritiesDataThread.call();
                    List<SecuritiesSinaFutureVo> securitiesSinaFutureVoList = redisCache.getCacheMapValue("money","securitiesSinaFutureVoList");
                    if (CollectionUtils.isEmpty(securitiesSinaFutureVoList)){
                        List<SecuritiesSinaFutureVo> list = new ArrayList<>();
                        list.add(securitiesSinaFutureVo);
                        redisCache.setCacheMapValue("money","securitiesSinaFutureVoList",list);
                    }else {
                        List<SecuritiesSinaFutureVo> collect = securitiesSinaFutureVoList.stream().filter(t -> t.getCode().equals(securitiesSinaFutureVo.getCode())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)){
                            securitiesSinaFutureVoList.remove(collect.get(0));
                        }
                        securitiesSinaFutureVoList.add(securitiesSinaFutureVo);
                        //按照振幅降序排序
                        securitiesSinaFutureVoList.sort(Comparator.comparing(SecuritiesSinaFutureVo::getPositiveNegativeFlag));
                        redisCache.setCacheMapValue("money","securitiesSinaFutureVoList",securitiesSinaFutureVoList);
                    }
                    if (1 == securitiesSinaFutureVo.getPositiveNegativeFlag() || 2 == securitiesSinaFutureVo.getPositiveNegativeFlag()){
                        String name = redisCache.getCacheObject(securitiesSinaFutureVo.getCode());
                        if (StringUtils.isEmpty(name)){
                            TbSinaFifteen tbSinaFifteen = new TbSinaFifteen();
                            tbSinaFifteen.setCode(securitiesSinaFutureVo.getCode());
                            tbSinaFifteen.setName(securitiesSinaFutureVo.getName());
                            tbSinaFifteen.setPoints(securitiesSinaFutureVo.getPrice());
                            Date date = new Date();
                            tbSinaFifteen.setLogDate(date);
                            tbSinaFifteen.setCreateTime(date);
                            iTbSinaFifteenService.insertTbSinaFifteen(tbSinaFifteen);
                            redisCache.setCacheObject(securitiesSinaFutureVo.getCode(),securitiesSinaFutureVo.getName(),5, TimeUnit.HOURS);
                        }
                    }
                    //判断当前振幅是否大于等于缓存中的
                    Thread.sleep(8000);
                }
                long endTime = System.currentTimeMillis();
                log.debug("执行时长：{}", endTime - startTime);
            }
        }catch (Exception e){
            log.info("异常：",e);
        }finally {
            if (b){
                lock.unlock();
                log.debug("logSina15 == 释放锁成功，休眠20秒");
                Thread.sleep(2,000);
                this.logSina15();
            }
        }
    }


}
