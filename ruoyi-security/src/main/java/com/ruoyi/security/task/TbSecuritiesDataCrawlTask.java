package com.ruoyi.security.task;

import com.ruoyi.security.service.ITbSecuritiesDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("tbSecuritiesDataCrawlTask")
@Slf4j
public class TbSecuritiesDataCrawlTask {


    @Autowired
    private ITbSecuritiesDataService tbSecuritiesDataService;

    public void execute() throws InterruptedException {
        tbSecuritiesDataService.crawl();
    }


}
