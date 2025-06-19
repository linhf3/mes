package com.ruoyi.security.task;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.enums.Constant;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.security.algorithm.CoreAlgorithmContet;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;

public class TbSecuritiesDataThread implements Callable<SecuritiesFutureVo> {

    private TbSecuritiesData tbSecuritiesData;

    private CoreAlgorithmContet coreAlgorithmContet;

    public TbSecuritiesDataThread(TbSecuritiesData tbSecuritiesData, CoreAlgorithmContet coreAlgorithmContet) {
        this.tbSecuritiesData = tbSecuritiesData;
        this.coreAlgorithmContet = coreAlgorithmContet;
    }

    @Override
    public SecuritiesFutureVo call() throws Exception {
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
        SecuritiesFutureVo securitiesFutureVo = new SecuritiesFutureVo();
        securitiesFutureVo.setCode(tbSecuritiesData.getCode());
        securitiesFutureVo.setName(tbSecuritiesData.getName());
        securitiesFutureVo.setExchangeCode(tbSecuritiesData.getExchangeCode());
        Map<String, Object> reMap = coreAlgorithmContet.deviationTheDayRate("futuresCoreAlgorithm", map);
        String proportion = (String) reMap.get("proportion");
        Double proportionDouble = Double.valueOf(proportion.replace("+", "").replace("%", ""));
        securitiesFutureVo.setProportion(proportion);
        Double dailySpread = (Double) reMap.get("dailySpread");
        Double price = (Double) reMap.get("price");
        Double min = (Double) reMap.get("min");
        Double max = (Double) reMap.get("max");
        securitiesFutureVo.setDailySpread(dailySpread);
        securitiesFutureVo.setPrice(price);
        securitiesFutureVo.setUndulate(tbSecuritiesData.getUndulate());
        Double theCurrentAmplitude1 = price-min;
        Double theCurrentAmplitude2 = max-price;
        securitiesFutureVo.setTheCurrentAmplitude(theCurrentAmplitude1>theCurrentAmplitude2?theCurrentAmplitude1:theCurrentAmplitude2);
        //获取策略，上下偏离
        Double deviation = null == tbSecuritiesData.getDeviation() || 0 == tbSecuritiesData.getDeviation() ? 100 : tbSecuritiesData.getDeviation();
        //点数振幅
        //Double undulate = null == tbSecuritiesData.getUndulate() || 0 == tbSecuritiesData.getUndulate()?0:tbSecuritiesData.getUndulate();
        Double undulate = tbSecuritiesData.getUndulate();
        if (null != undulate){
            if (theCurrentAmplitude2 >= undulate) {
                securitiesFutureVo.setPositiveNegativeFlag(4);
            }else if (theCurrentAmplitude1 >= undulate) {
                securitiesFutureVo.setPositiveNegativeFlag(3);
            }
        }else {
            if (proportionDouble <= -deviation){
                securitiesFutureVo.setPositiveNegativeFlag(2);
            }else if (proportionDouble > deviation){
                securitiesFutureVo.setPositiveNegativeFlag(1);
            }else {
                securitiesFutureVo.setPositiveNegativeFlag(0);
            }
        }
        return securitiesFutureVo;
    }
}
