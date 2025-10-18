package com.ruoyi.security.loader;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.security.domain.TbFluctuationLog;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.service.ITbFluctuationLogService;
import com.ruoyi.security.service.ITbSecuritiesDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SecurityLoader implements CommandLineRunner {

    @Autowired
    private ITbFluctuationLogService iTbFluctuationLogService;

    @Autowired
    private ITbSecuritiesDataService iTbSecuritiesDataService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        TbFluctuationLog tbFluctuationLog = new TbFluctuationLog();
        List<TbFluctuationLog> tbFluctuationLogs = iTbFluctuationLogService.selectTbFluctuationLogList(tbFluctuationLog);
        if (CollectionUtils.isEmpty(tbFluctuationLogs)){
            return;
        }
        // 分组并提取 value，然后降序排序
        Map<String, List<Double>> sortedValuesByGroup = tbFluctuationLogs.stream()
                .collect(Collectors.groupingBy(
                        TbFluctuationLog::getCode,
                        Collectors.mapping(
                                TbFluctuationLog::getUndulate,
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().stream()
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList())))
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue
                ));

        redisCache.deleteCacheMapValue("money","sortedValuesByGroup");
        redisCache.setCacheMapValue("money","sortedValuesByGroup",sortedValuesByGroup);
        // 分组并获取每个组降序排序后的第10个元素，最后转换为 Map<String, Double>
        Map<String, Double> minValues = tbFluctuationLogs.stream()
                .collect(Collectors.groupingBy(
                        TbFluctuationLog::getCode,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    list.sort(Comparator.comparingDouble(TbFluctuationLog::getUndulate).reversed());
                                    return list.size() >= 10 ? list.get(9).getUndulate() : null;
                                }
                        )
                ));
        //存到缓存中
        redisCache.deleteCacheMapValue("money","f_xinnamin");
        redisCache.setCacheMapValue("money","f_xinnamin",minValues);
        //查询所有的新浪数据转换为东方财富编码
        TbSecuritiesData tbSecuritiesData = new TbSecuritiesData();
        List<TbSecuritiesData> tbSecuritiesDataList = iTbSecuritiesDataService.selectTbSecuritiesDataList(tbSecuritiesData);
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return;
        }
        List<TbSecuritiesData> tbSecuritiesDataSinaList = tbSecuritiesDataList.stream().filter(t -> 2 == t.getType() && StringUtils.isNotEmpty(t.getExchangeCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tbSecuritiesDataSinaList)){
            return;
        }
        redisCache.deleteCacheMapValue("money","f_sina_tbSecuritiesDataList");
        redisCache.setCacheMapValue("money","f_sina_tbSecuritiesDataList",tbSecuritiesDataSinaList);
        // 将 List 转换为 Map
        Map<String, String> map = tbSecuritiesDataSinaList.stream()
                .collect(Collectors.toMap(
                        TbSecuritiesData::getCode, // 键
                        TbSecuritiesData::getExchangeCode // 值
                ));
        // 将 List 转换为 Map
        Map<String, String> dongfangMap = tbSecuritiesDataSinaList.stream()
                .collect(Collectors.toMap(
                        TbSecuritiesData::getExchangeCode, // 键
                        TbSecuritiesData::getCode // 值
                ));
        //存到缓存中
        redisCache.deleteCacheMapValue("money","f_dongfang");
        redisCache.setCacheMapValue("money","f_dongfang",dongfangMap);
        redisCache.deleteCacheMapValue("money","f_sina");
        redisCache.setCacheMapValue("money","f_sina",map);
        //转换
        Map<String, Double> m = new HashMap<>();
        for (Map.Entry<String,Double> entry:minValues.entrySet()){
            String code = map.get(entry.getKey());
            if (StringUtils.isNotEmpty(code)){
                m.put(code,entry.getValue());
            }
        }
        if (CollectionUtils.isEmpty(m)){
            return;
        }
        //存到缓存中
        redisCache.deleteCacheMapValue("money","f_dongfangmin");
        redisCache.setCacheMapValue("money","f_dongfangmin",m);
        //东方数据放入缓存
        List<TbSecuritiesData> tbSecuritiesDataDongfangList = tbSecuritiesDataList.stream().filter(t -> 1 == t.getType()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tbSecuritiesDataDongfangList)){
            redisCache.deleteCacheMapValue("money","f_dongfang_tbSecuritiesDataList");
            redisCache.setCacheMapValue("money","f_dongfang_tbSecuritiesDataList",tbSecuritiesDataDongfangList);
        }
        redisCache.deleteCacheMapValue("money","securitiesSinaFutureVoList");
    }
}
