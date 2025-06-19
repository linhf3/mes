package com.ruoyi.security.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 股票期货策略接口环境类
 * @author sie_linhongfei
 * @createDate 2022/07/09 10:27
 */
@Service
public class CoreAlgorithmContet {

    @Autowired
    private Map<String, CoreAlgorithm> coreAlgorithmMap;

    public Map<String, Object> deviationTheDayRate(String beanName, Map map) {
        return coreAlgorithmMap.get(beanName).deviationTheDayRate(map);
    };

}
