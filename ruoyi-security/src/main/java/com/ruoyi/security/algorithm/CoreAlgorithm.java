package com.ruoyi.security.algorithm;

import java.util.Map;

/**
 * 股票期货策略接口
 * @author sie_linhongfei
 * @createDate 2022/07/09 10:27
 */
public interface CoreAlgorithm {

    /**
     * 获取当日偏离数据
     * @param map
     * @return Map<String, Object>
     */
    Map<String, Object> deviationTheDayRate(Map map);

}
