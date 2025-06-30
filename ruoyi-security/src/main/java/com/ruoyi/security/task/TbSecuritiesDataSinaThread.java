package com.ruoyi.security.task;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.enums.Constant;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.security.algorithm.CoreAlgorithmContet;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.vo.SecuritiesSinaFutureVo;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class TbSecuritiesDataSinaThread implements Callable<SecuritiesSinaFutureVo> {

    private TbSecuritiesData tbSecuritiesData;

    private CoreAlgorithmContet coreAlgorithmContet;

    private String url;

    // 定义时间格式
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TbSecuritiesDataSinaThread(TbSecuritiesData tbSecuritiesData, CoreAlgorithmContet coreAlgorithmContet,String url) {
        this.tbSecuritiesData = tbSecuritiesData;
        this.coreAlgorithmContet = coreAlgorithmContet;
        this.url = url;
    }

    @Override
    public SecuritiesSinaFutureVo call() throws Exception {
        //拼接地址
        Map urlMap = new HashMap<>();
        urlMap.put("variety1", tbSecuritiesData.getCode());
        urlMap.put("variety2", System.currentTimeMillis());
        urlMap.put("variety3", tbSecuritiesData.getCode());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(new StrSubstitutor(urlMap).replace(url));
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");
        httpGet.setHeader("Referer", new StrSubstitutor(urlMap).replace(Constant.REFERER));
        httpGet.setHeader("Accept", "*/*");
        httpGet.setHeader(":authority", "stock2.finance.sina.com.cn");
        httpGet.setHeader("cookie", "UOR=www.baidu.com,finance.sina.com.cn,; SFA_version8.8.0=2025-03-04%2022%3A03; SINAGLOBAL=117.61.111.242_1741097176.634996; U_TRS1=000000f2.89225770.67c708dd.ad19008d; SFA_version8.9.0=2025-03-15%2013%3A54; Apache=117.61.85.101_1742018202.401408; SFA_version8.9.0_click=1; hqEtagMode=1; rotatecount=2; ULV=1742018211635:4:4:2:117.61.85.101_1742018202.401408:1742018199525; U_TRS2=00000065.45e175a.67d516a6.48bc3ee3; FIN_ALL_VISITED=HC0%2CMA0%2CTA0%2CV0%2CFU0%2CLC2602%2CRB0%2CCF2601%2CRM2505%2CRB2505%2CCY0; NEWESTVISITED_FUTURE=%7B%22code%22%3A%22HC0%22%2C%22hqcode%22%3A%22nf_HC0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22MA0%22%2C%22hqcode%22%3A%22nf_MA0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22TA0%22%2C%22hqcode%22%3A%22nf_TA0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22V0%22%2C%22hqcode%22%3A%22nf_V0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22FU0%22%2C%22hqcode%22%3A%22nf_FU0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22LC2602%22%2C%22hqcode%22%3A%22nf_LC2602%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22RB0%22%2C%22hqcode%22%3A%22nf_RB0%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22CF2601%22%2C%22hqcode%22%3A%22nf_CF2601%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22RM2505%22%2C%22hqcode%22%3A%22nf_RM2505%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22RB2505%22%2C%22hqcode%22%3A%22nf_RB2505%22%2C%22type%22%3A1%7D%7C%7B%22code%22%3A%22CY0%22%2C%22hqcode%22%3A%22nf_CY0%22%2C%22type%22%3A1%7D");
        //发送http请求
        HttpResponse response = httpClient.execute(httpGet);
        String rx = EntityUtils.toString(response.getEntity());//HttpUtils.sendGet(new StrSubstitutor(urlMap).replace(Constant.SINA_FIFTEEN_MIN_LINE));
        String str = rx.substring(rx.indexOf("(") + 1, rx.lastIndexOf(")"));
        JSONArray jsonArray = JSONArray.parse(str);

        int period = 20; // 布林带计算周期
        double k = 2.0; // 标准差倍数

        List<Double> mb = new ArrayList<>();
        List<Double> ub = new ArrayList<>();
        List<Double> lb = new ArrayList<>();
        double price = 0.0d;
        double oprice = 0.0d;
        List<Double> prices = new ArrayList<>();
        for (int i = jsonArray.size() - 1; i >= jsonArray.size() - 20; i--) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String c = (String) jsonObject.get("c");
            Double pri = Double.valueOf(c);
            prices.add(pri);
            if (i == jsonArray.size()-1) {
                price = pri;
                oprice = Double.valueOf((String) jsonObject.get("o"));
            }
        }
        double sma = calculateSMA(prices, 20, period);
        double std = calculateStandardDeviation(prices, 20, period, sma);

        mb.add(sma);
        ub.add(sma + k * std);
        lb.add(sma - k * std);
        Double uprice = ub.get(0);
        Double mprice = mb.get(0);
        Double lprice = lb.get(0);
        SecuritiesSinaFutureVo securitiesSinaFutureVo = new SecuritiesSinaFutureVo();
        securitiesSinaFutureVo.setTime(LocalDateTime.now().format(formatter));
        securitiesSinaFutureVo.setPrice(price);
        securitiesSinaFutureVo.setCode(tbSecuritiesData.getCode());
        securitiesSinaFutureVo.setName(tbSecuritiesData.getName());
        if (price >= uprice && oprice >= uprice){
            securitiesSinaFutureVo.setMsg("上上");
            securitiesSinaFutureVo.setPositiveNegativeFlag(1);
        }else if (price >= uprice){
            securitiesSinaFutureVo.setMsg("上");
            securitiesSinaFutureVo.setPositiveNegativeFlag(2);
        }else if (price>mprice && price<uprice){
            securitiesSinaFutureVo.setMsg("中上");
            securitiesSinaFutureVo.setPositiveNegativeFlag(3);
        }else if (price <= lprice && oprice <= lprice){
            securitiesSinaFutureVo.setMsg("下下");
            securitiesSinaFutureVo.setPositiveNegativeFlag(4);
        }else if (price < lprice){
            securitiesSinaFutureVo.setMsg("下");
            securitiesSinaFutureVo.setPositiveNegativeFlag(5);
        }else if (price==mprice){
            securitiesSinaFutureVo.setMsg("中");
            securitiesSinaFutureVo.setPositiveNegativeFlag(6);
        }else if (price<mprice && price>lprice){
            securitiesSinaFutureVo.setMsg("中下");
            securitiesSinaFutureVo.setPositiveNegativeFlag(7);
        }
        return securitiesSinaFutureVo;
    }

    // 计算简单移动平均线
    private static double calculateSMA(List<Double> prices, int currentIndex, int period) {
        double sum = 0;
        for (int i = currentIndex - period; i < currentIndex; i++) {
            sum += prices.get(i);
        }
        return sum / period;
    }

    // 计算标准差
    private static double calculateStandardDeviation(List<Double> prices, int currentIndex, int period, double sma) {
        double sum = 0;
        for (int i = currentIndex - period; i < currentIndex; i++) {
            sum += Math.pow(prices.get(i) - sma, 2);
        }
        return Math.sqrt(sum / period);
    }
}
