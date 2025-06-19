package com.ruoyi.security.task;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.security.algorithm.CoreAlgorithmContet;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.mapper.TbSecuritiesDataMapper;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;


@Component("tbSecuritiesDataTask")
@Slf4j
public class TbSecuritiesDataTask {

    @Autowired
    private TbSecuritiesDataMapper tbSecuritiesDataMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CoreAlgorithmContet coreAlgorithmContet;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    ReentrantLock lock = new ReentrantLock();

    public void execute() throws InterruptedException {

        boolean b = lock.tryLock();
        boolean f = false;
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
                f = true;
                //1.查询有效配置
                List<TbSecuritiesData> tbSecuritiesDataList = redisCache.getCacheList("tbSecuritiesDataList");
                if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
                    TbSecuritiesData tbSecuritiesData = new TbSecuritiesData();
                    tbSecuritiesData.setType(1);
                    tbSecuritiesData.setStatus(0);
                    tbSecuritiesDataList = tbSecuritiesDataMapper.selectTbSecuritiesDataList(tbSecuritiesData);
                    if (!CollectionUtils.isEmpty(tbSecuritiesDataList)){
                        redisCache.setCacheList("tbSecuritiesDataList",tbSecuritiesDataList);
                    }
                }
                if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
                    return;
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
                if (!CollectionUtils.isEmpty(list) && list.get(0) == null){
                    return;
                }
                long endTime = System.currentTimeMillis();
                log.debug("执行时长：{}", endTime - startTime);
                //按照振幅降序排序
                list.sort(Comparator.comparing(SecuritiesFutureVo::getDailySpread).reversed());
                //4、存到redis中
                redisCache.setCacheMapValue("money","securitiesFutureVoList",list);
            }
        }catch (Exception e){
            log.info("异常：",e);
        }finally {
            if (b){
                lock.unlock();
            }
            if (f){
                Thread.sleep(10000);
                this.execute();
            }
        }
    }


}
