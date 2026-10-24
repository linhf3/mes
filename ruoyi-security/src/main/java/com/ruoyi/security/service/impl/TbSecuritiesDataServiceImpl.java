package com.ruoyi.security.service.impl;

import java.time.LocalDate;
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
import com.ruoyi.security.loader.SecurityLoader;
import com.ruoyi.security.service.ITbFluctuationLogService;
import com.ruoyi.security.service.ITbSecuritiesHistoryService;
import com.ruoyi.security.service.ITbSinaFifteenService;
import com.ruoyi.security.task.FutureThread;
import com.ruoyi.security.task.TbSecuritiesDataSinaThread;
import com.ruoyi.security.task.TbSecuritiesDataThread;
import com.ruoyi.security.vo.FutureVo;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import com.ruoyi.security.vo.SecuritiesSinaFutureVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import com.ruoyi.security.mapper.TbSecuritiesDataMapper;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.service.ITbSecuritiesDataService;
import org.springframework.util.CollectionUtils;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

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

    @Autowired
    private SecurityLoader securityLoader;

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
        tbSecuritiesDataMapper.insertTbSecuritiesData(tbSecuritiesData);
        tbSecuritiesDataMapper.updateTbSecuritiesData(tbSecuritiesData);
        return 1;
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
        tbSecuritiesDataMapper.updateTbSecuritiesData(tbSecuritiesData);
        updateRedisKey();
        return 1;
    }

    private void updateRedisKey(){
        TbSecuritiesData tbSecuritiesData = new TbSecuritiesData();
        List<TbSecuritiesData> tbSecuritiesDataList = this.selectTbSecuritiesDataList(tbSecuritiesData);
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return;
        }
        List<TbSecuritiesData> tbSecuritiesDataSinaList = tbSecuritiesDataList.stream().filter(t -> 2 == t.getType() && 0 == t.getStatus() && StringUtils.isNotEmpty(t.getExchangeCode())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tbSecuritiesDataSinaList)){
            redisCache.deleteCacheMapValue("money","f_sina_tbSecuritiesDataList");
            redisCache.setCacheMapValue("money","f_sina_tbSecuritiesDataList",tbSecuritiesDataSinaList);
        }
         //东方数据放入缓存
        List<TbSecuritiesData> tbSecuritiesDataDongfangList = tbSecuritiesDataList.stream().filter(t -> 1 == t.getType()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tbSecuritiesDataDongfangList)){
            redisCache.deleteCacheMapValue("money","f_dongfang_tbSecuritiesDataList");
            redisCache.setCacheMapValue("money","f_dongfang_tbSecuritiesDataList",tbSecuritiesDataDongfangList);
        }
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
        tbSecuritiesDataMapper.deleteTbSecuritiesDataByIds(ids);
        updateRedisKey();
        return 1;

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
        tbSecuritiesDataMapper.deleteTbSecuritiesDataById(id);
        updateRedisKey();
        return 1;
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
    public List<FutureVo> findListByPoints() {
        //1.查询有效配置
        List<TbSecuritiesData> tbSecuritiesDataList = redisCache.getCacheMapValue("money","f_dongfang_tbSecuritiesDataList");
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return new ArrayList<>();
        }
        tbSecuritiesDataList = tbSecuritiesDataList.stream().filter(t -> t.getStatus() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return new ArrayList<>();
        }
        Map<String, List<Double>> sortedValuesByGroup = redisCache.getCacheMapValue("money","sortedValuesByGroup");
        Map<String,String> dongfangMap = redisCache.getCacheMapValue("money", "f_dongfang");
        List<Future<FutureVo>> futures = new ArrayList<>();
        //2、构建线程
        long startTime = System.currentTimeMillis();
        for (TbSecuritiesData tbSecuritiesData : tbSecuritiesDataList) {
            FutureThread tbSecuritiesDataThread = new FutureThread(tbSecuritiesData, coreAlgorithmContet, sortedValuesByGroup.get(dongfangMap.get(tbSecuritiesData.getCode())));
            Future<FutureVo> future = taskExecutor.submit(tbSecuritiesDataThread);
            futures.add(future);
        }
        //3、等待所有任务完成
        List<FutureVo> list = new ArrayList<>();
        for (Future<FutureVo> future : futures) {
            try {
                list.add(future.get());// 阻塞直到任务完成
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 自定义降序排序
        list = list.stream()
                .sorted(
                        comparing(FutureVo::getNum, nullsLast(Integer::compareTo))  // ① 第一关键字：num 倒序
                                .thenComparing(FutureVo::getTheCurrentAmplitude)                 // ② 第二关键字：振幅
                                .thenComparing(FutureVo::getDailySpread)
                )
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info("执行时长：{}", endTime - startTime);
        return list;
    }

    @Override
    public List<SecuritiesSinaFutureVo> findSinaList() {
        List<SecuritiesSinaFutureVo>  securitiesSinaFutureVoList = redisCache.getCacheMapValue("money", "securitiesSinaFutureVoList");
        return CollectionUtils.isEmpty(securitiesSinaFutureVoList)?new ArrayList<>():securitiesSinaFutureVoList;
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
            String formattedDate = currentDate.minusDays(1).format(formatter);
            Map urlMap = new HashMap<>();
            DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (TbSecuritiesData tbSecuritiesData:tbSecuritiesSinaDataList){
                List<TbFluctuationLog> list = new ArrayList<>();
                urlMap.clear();
                urlMap.put("variety1", new StringBuilder(tbSecuritiesData.getCode()).append("_").append(formattedDate).toString());
                urlMap.put("variety2", tbSecuritiesData.getCode());
                // 创建HttpClient实例
                HttpClient httpClient = HttpClients.createDefault();
                // 创建HttpGet实例
                HttpGet httpGet = new HttpGet(new StrSubstitutor(urlMap).replace(Constant.SINA_DATE_DATA));
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
                // 发送请求
                HttpResponse response = httpClient.execute(httpGet);
                // 获取响应体
                HttpEntity entity = response.getEntity();
                String rx = EntityUtils.toString(entity, "UTF-8");
                //String rx = HttpUtils.sendGet(new StrSubstitutor(urlMap).replace(Constant.SINA_DATE_DATA));
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
                Thread.sleep(20000);
            }
            //更新redis
            securityLoader.run();
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
    public boolean getQuheqihuoDataFromCom() {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://api.jijinhao.com/quoteCenter/realTime.htm?codes=JO_165668,JO_233734,JO_165809,JO_166029,JO_291841,JO_165822,JO_233664,JO_166093,JO_165874,JO_233677,JO_233760,JO_233773,JO_166016,JO_166055,JO_166132,JO_166171,JO_166145,JO_233690,JO_166210,JO_166158,JO_165887,JO_165796,JO_166184,JO_165990,JO_166003,JO_166119,JO_165835,JO_291821,JO_166249,JO_233747,JO_291828,JO_291854,JO_165694,JO_165770,JO_166257,JO_165732,JO_165757,JO_166080,JO_233708,JO_165848,JO_165861,JO_165900,JO_165925,JO_165951,JO_166042,JO_165964,JO_165938,JO_166106,JO_166197,JO_291867,JO_165655,JO_165629,JO_165642,JO_344228,JO_165616,JO_165590,JO_165603,JO_291808,JO_165681,JO_165977,JO_165719,JO_233721,JO_165783,JO_166223,JO_166236,JO_322422,JO_335884,JO_336104,JO_336117,JO_336321,JO_336611,JO_336625,JO_343068,JO_92270,JO_92268,JO_92266,JO_92264,JO_92262,JO_92260,JO_12553,JO_12552,JO_92232,JO_92233,JO_92230,JO_92229,JO_50814,JO_42634,JO_42638,JO_42660,JO_42657,JO_42653,JO_321453,JO_321455,JO_321465,JO_321466,JO_42625,JO_52670,JO_52678,JO_42632,JO_56044,JO_338490,JO_338504,JO_338518,JO_338532,JO_338546,JO_338562,JO_338728,JO_338594,JO_50771,JO_50797,JO_50842,JO_50780,JO_50789,JO_50848,JO_108893,JO_108878,JO_343878,JO_346070,JO_347000,JO_347231,JO_347500,JO_348610,JO_348624&_="+System.currentTimeMillis());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // ===== 请求头 =====
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/153.0.0.0 Safari/537.36");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Referer", "https://www.quheqihuo.com/quote/zhuli.html");
            conn.setRequestProperty("sec-ch-ua",
                    "\"Google Chrome\";v=\"153\", \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"153\"");
            conn.setRequestProperty("sec-ch-ua-mobile", "?0");

            int code = conn.getResponseCode();
            System.out.println("Content-Length=" + conn.getHeaderField("Content-Length"));
            System.out.println("Content-Encoding=" + conn.getHeaderField("Content-Encoding"));
            InputStream in = (code >= 200 && code < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            log.info("返回值：" + sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("HTTP GET 请求失败: ", e);
        } finally {
            closeQuietly(reader);
            if (conn != null) conn.disconnect();
        }
        return true;
    }

    @Async("taskExecutor")
    @Override
    public void logSina15(String flag) throws InterruptedException {
        log.debug("=====================开始执行logSina15=====================");
        redisCache.setCacheMapValue("money","logSina15", flag);
        try {
            if (!"start".equals(redisCache.getCacheMapValue("money","logSina15"))){
                return;
            }
            List<TbSecuritiesData> tbSecuritiesDataList = redisCache.getCacheMapValue("money","f_sina_tbSecuritiesDataList");
            if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
                return;
            }
            log.debug("=====================redis查询redisSecuritiesSinaFutureVo=====================");
            List<SecuritiesSinaFutureVo> redisSecuritiesSinaFutureVo = redisCache.getCacheMapValue("money", "securitiesSinaFutureVoList");
            if (!CollectionUtils.isEmpty(redisSecuritiesSinaFutureVo)){
                List<String> codeList = tbSecuritiesDataList.stream().map(TbSecuritiesData::getCode).collect(Collectors.toList());
                List<SecuritiesSinaFutureVo> list = redisSecuritiesSinaFutureVo.stream().filter(t -> codeList.contains(t.getCode())).collect(Collectors.toList());
                log.debug("=====================redis设置redisSecuritiesSinaFutureVo(list)=====================");
                redisCache.setCacheMapValue("money", "securitiesSinaFutureVoList",CollectionUtils.isEmpty(list)?new ArrayList<>():list);
            }
            long startTime = System.currentTimeMillis();
            log.debug("=====================redisSecuritiesSinaFutureVo大小：{}：",tbSecuritiesDataList.size());
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
                log.debug("=====================进入休眠=====================");
                Thread.sleep(6000);
            }
            long endTime = System.currentTimeMillis();
            log.debug("执行时长：{}", endTime - startTime);
            Thread.sleep(1000);
            log.debug("=====================递归=====================");
            logSina15(redisCache.getCacheMapValue("money","logSina15"));
        }catch (Exception e){
            log.info("异常：",e);
        }
    }


}
