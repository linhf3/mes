package com.ruoyi.common.enums;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author sie_linhongfei
 * @createDate 2021/11/3 21:27
 */
public class Constant {

    public static final String  LOCALURL = "http://localhost:8081";

    public static final String  QQ = "191663059@qq.com";

    //所有股票数据
    public static final String url = "https://3.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112405599291885129443_1649862255846&pn=1&pz=5000&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1649862255847";

    //五日股票详细数据
    public static final String fiveDaysUrl = "http://push2his.eastmoney.com/api/qt/stock/trends2/get?fields1=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13&fields2=f51,f52,f53,f54,f55,f56,f57,f58&ndays=5&iscr=0&iscca=0&secid=";

    //当日股票数据
    public static final String oneDaysUrl = "http://push2his.eastmoney.com/api/qt/stock/trends2/get?fields1=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13&fields2=f51,f52,f53,f54,f55,f56,f57,f58&ndays=1&iscr=0&iscca=0&secid=";

    //期货一天明细数据
    public static final String futuresUrl ="http://push2.eastmoney.com/api/qt/stock/trends2/get?secid=${futuresUrl}&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6%2Cf7%2Cf8%2Cf9%2Cf10%2Cf11%2Cf12%2Cf13&fields2=f51%2Cf53%2Cf54%2Cf55%2Cf58";

    //期货主力数据
    public static final String FUTURESMAINFORCEURL ="https://futsseapi.eastmoney.com/list/${place}?orderBy=zdf&sort=desc&pageSize=5000&pageIndex=0";

    //新浪日k
    public static final String SINADATEURL ="http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=${variety}";

    //外汇
    public static final String FOREIGN_EXCHANGE_URL ="http://push2.eastmoney.com/api/qt/stock/trends2/get?secid=${place}&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6%2Cf7%2Cf11%2Cf12%2Cf13%2Cf14&fields2=f51%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58&ndays=1";

    public static final String SINA_DATE_DATA = "https://stock2.finance.sina.com.cn/futures/api/jsonp.php/var%20_${variety1}=/InnerFuturesNewService.getDailyKLine?symbol=${variety2}";

    public static SimpleDateFormat slf = new SimpleDateFormat("yyyy-MM-dd");

    public static DecimalFormat format = new DecimalFormat("#.00");

    //新浪实时数据
    //1、当日分时图数据
    public static final String SINA_MIN_LINE = "http://stock2.finance.sina.com.cn/futures/api/jsonp.php/var=/InnerFuturesNewService.getMinLine?symbol=${variety}";
    //2、五日分时图数据
    public static final String SINA_FOUR_DAYS_LINE = "http://stock2.finance.sina.com.cn/futures/api/jsonp.php/var=/InnerFuturesNewService.getFourDaysLine?symbol=${variety}";
    //3、60分钟数据
    public static final String SINA_FEW_MIN_LINE = "http://stock2.finance.sina.com.cn/futures/api/jsonp.php/var=/InnerFuturesNewService.getFewMinLine?symbol=${variety}&type=60";
    //4、15分钟数据
    public static final String SINA_FIFTEEN_MIN_LINE = "http://stock2.finance.sina.com.cn/futures/api/jsonp.php/var%20_${variety1}_15_${variety2}/InnerFuturesNewService.getFewMinLine?symbol=${variety3}&type=15";
    //5、日线数据(只能获取到前一日数据，前一天收盘后价格)
    public static final String SINA_DAILY_KLINE = "http://stock2.finance.sina.com.cn/futures/api/jsonp.php/var=/InnerFuturesNewService.getDailyKLine?symbol=${variety}";
    public static final String REFERER = "https://finance.sina.com.cn/futures/quotes/{variety3}.shtml";


    //btc东方财富一分钟，当日交易 2023-06-30 21:02,7386,7390,7380,7379.5 时间 - 收盘价-最高价-最低价-均价
    public static final String URL = "http://push2.eastmoney.com/api/qt/stock/trends2/get?secid=${secid}&fields1=f5%2Cf8%2Cf17&fields2=f51%2Cf53%2Cf54%2Cf55%2Cf58";
    public static final String HOLIDAY = "http://timor.tech/api/holiday/info/${variety}";

}
