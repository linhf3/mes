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
        //查询所有的新浪数据转换为东方财富编码
        TbSecuritiesData tbSecuritiesData = new TbSecuritiesData();
        tbSecuritiesData.setType(2);
        List<TbSecuritiesData> tbSecuritiesDataList = iTbSecuritiesDataService.selectTbSecuritiesDataList(tbSecuritiesData);
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return;
        }
        tbSecuritiesDataList = tbSecuritiesDataList.stream().filter(t -> StringUtils.isNotEmpty(t.getExchangeCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tbSecuritiesDataList)){
            return;
        }
        // 将 List 转换为 Map
        Map<String, String> map = tbSecuritiesDataList.stream()
                .collect(Collectors.toMap(
                        TbSecuritiesData::getCode, // 键
                        TbSecuritiesData::getExchangeCode // 值
                ));
        // 将 List 转换为 Map
        Map<String, String> xinNamap = tbSecuritiesDataList.stream()
                .collect(Collectors.toMap(
                        TbSecuritiesData::getExchangeCode, // 键
                        TbSecuritiesData::getCode // 值
                ));
        //存到缓存中
        redisCache.setCacheMap("f_dongfang",xinNamap);
        redisCache.setCacheMap("f_sina",map);
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
        redisCache.setCacheMap("f_recentMinValueMap",m);
    }
}
