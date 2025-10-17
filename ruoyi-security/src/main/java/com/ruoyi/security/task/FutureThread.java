package com.ruoyi.security.task;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.enums.Constant;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.security.algorithm.CoreAlgorithmContet;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.vo.FutureVo;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FutureThread implements Callable<FutureVo> {

    private TbSecuritiesData tbSecuritiesData;

    private CoreAlgorithmContet coreAlgorithmContet;

    public FutureThread(TbSecuritiesData tbSecuritiesData, CoreAlgorithmContet coreAlgorithmContet) {
        this.tbSecuritiesData = tbSecuritiesData;
        this.coreAlgorithmContet = coreAlgorithmContet;
    }

    @Override
    public FutureVo call() throws Exception {
        //拼接地址
        Map urlMap = new HashMap<>();
        urlMap.put("futuresUrl", tbSecuritiesData.getExchangeCode());
        //发送http请求
        String rx = HttpUtils.sendGet(new StrSubstitutor(urlMap).replace(Constant.futuresUrl));
        Map node = (Map) JSON.parse(rx);
        //数据集
        Map map = (Map) node.get("data");
        List trendsM = (List) map.get("trends");
        //白天开盘，无数据的情况（晚上）
        if (CollectionUtils.isEmpty(trendsM)) {
            return null;
        }
        FutureVo futureVo = new FutureVo();
        futureVo.setName(tbSecuritiesData.getName());
        Map<String, Object> reMap = coreAlgorithmContet.deviationTheDayRate("futuresCoreAlgorithm", map);
        String proportion = (String) reMap.get("proportion");
        futureVo.setProportion(proportion);
        Double dailySpread = (Double) reMap.get("dailySpread");
        Double price = (Double) reMap.get("price");
        Double min = (Double) reMap.get("min");
        Double max = (Double) reMap.get("max");
        futureVo.setDailySpread(dailySpread);
        futureVo.setPrice(price);
        Double theCurrentAmplitude1 = price-min;
        Double theCurrentAmplitude2 = max-price;
        futureVo.setTheCurrentAmplitude(theCurrentAmplitude1>theCurrentAmplitude2?theCurrentAmplitude1:theCurrentAmplitude2);
        futureVo.setNum(12);
        return futureVo;
    }
}
