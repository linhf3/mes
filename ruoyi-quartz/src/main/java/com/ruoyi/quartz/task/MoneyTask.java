package com.ruoyi.quartz.task;


import com.ruoyi.security.service.ITbSecuritiesDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("moneyTask")
@Slf4j
public class MoneyTask {

    @Autowired
    private ITbSecuritiesDataService iTbSecuritiesDataService;

    public void synSinaFiveData(){
        try {
            while (true){
                iTbSecuritiesDataService.synSinaFiveData();
                Thread.sleep(300000);
            }
        }catch (Exception e){
            log.error("同步异常");
        }

    }

}
