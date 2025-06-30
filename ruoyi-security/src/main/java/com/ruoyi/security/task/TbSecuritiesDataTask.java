//package com.ruoyi.security.task;
//
//import com.ruoyi.common.core.redis.RedisCache;
//import com.ruoyi.common.enums.Constant;
//import com.ruoyi.security.algorithm.CoreAlgorithmContet;
//import com.ruoyi.security.domain.TbSecuritiesData;
//import com.ruoyi.security.mapper.TbSecuritiesDataMapper;
//import com.ruoyi.security.vo.SecuritiesFutureVo;
//import com.ruoyi.security.vo.SecuritiesSinaFutureVo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.concurrent.locks.ReentrantLock;
//import java.util.stream.Collectors;
//
//
//@Component("tbSecuritiesDataTask")
//@Slf4j
//public class TbSecuritiesDataTask {
//
//    @Autowired
//    private TbSecuritiesDataMapper tbSecuritiesDataMapper;
//
//    @Autowired
//    private RedisCache redisCache;
//
//    @Autowired
//    private CoreAlgorithmContet coreAlgorithmContet;
//
//    ReentrantLock lock = new ReentrantLock();
//
//    public void execute() throws InterruptedException {
//
//        boolean b = lock.tryLock();
//        if (!b){
//            return;
//        }
//        try {
//            // 获取当前时间
//            LocalTime now = LocalTime.now();
//            //判断是否是周末
//            DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
////            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
////                return;
////            }
//            // 定义目标时间 11:35
//            LocalTime targetTime1 = LocalTime.of(9, 00);
//            LocalTime targetTime2 = LocalTime.of(11, 31);
//            LocalTime targetTime3 = LocalTime.of(13, 30);
//            LocalTime targetTime4 = LocalTime.of(15, 01);
//            LocalTime targetTime5 = LocalTime.of(21, 00);
//            LocalTime targetTime6 = LocalTime.of(23, 00);
//            if ((now.isAfter(targetTime1) && now.isBefore(targetTime2)) || (now.isAfter(targetTime3) && now.isBefore(targetTime4))
//                    || (now.isAfter(targetTime5) && now.isBefore(targetTime6))){
//                List<TbSecuritiesData> tbSecuritiesDataList = redisCache.getCacheMapValue("money","f_sina_tbSecuritiesDataList");
//                if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
//                    return;
//                }
//                long startTime = System.currentTimeMillis();
//                for (TbSecuritiesData tbSecuritiesData : tbSecuritiesDataList) {
//                    TbSecuritiesDataSinaThread tbSecuritiesDataThread = new TbSecuritiesDataSinaThread(tbSecuritiesData, coreAlgorithmContet, Constant.SINA_FIFTEEN_MIN_LINE);
//                    SecuritiesSinaFutureVo securitiesSinaFutureVo = tbSecuritiesDataThread.call();
//                    List<SecuritiesSinaFutureVo> securitiesSinaFutureVoList = redisCache.getCacheMapValue("money","securitiesSinaFutureVoList");
//                    if (CollectionUtils.isEmpty(securitiesSinaFutureVoList)){
//                        List<SecuritiesSinaFutureVo> list = new ArrayList<>();
//                        list.add(securitiesSinaFutureVo);
//                        redisCache.setCacheMapValue("money","securitiesSinaFutureVoList",list);
//                    }else {
//                        List<SecuritiesSinaFutureVo> collect = securitiesSinaFutureVoList.stream().filter(t -> t.getCode().equals(securitiesSinaFutureVo.getCode())).collect(Collectors.toList());
//                        if (!CollectionUtils.isEmpty(collect)){
//                            securitiesSinaFutureVoList.remove(collect.get(0));
//                        }
//                        securitiesSinaFutureVoList.add(securitiesSinaFutureVo);
//                        //按照振幅降序排序
//                        securitiesSinaFutureVoList.sort(Comparator.comparing(SecuritiesSinaFutureVo::getPositiveNegativeFlag));
//                        redisCache.setCacheMapValue("money","securitiesSinaFutureVoList",securitiesSinaFutureVoList);
//                    }
//                    //判断当前振幅是否大于等于缓存中的
//                    Thread.sleep(8000);
//                }
//                long endTime = System.currentTimeMillis();
//                log.debug("执行时长：{}", endTime - startTime);
//            }
//        }catch (Exception e){
//            log.info("异常：",e);
//        }finally {
//            if (b){
//                lock.unlock();
//                Thread.sleep(2,000);
//                this.execute();
//            }
//        }
//    }
//
//
//}
