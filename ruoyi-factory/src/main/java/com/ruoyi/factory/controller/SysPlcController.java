package com.ruoyi.factory.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.factory.service.ISysPlcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/plc")
public class SysPlcController extends BaseController {

    @Autowired
    private ISysPlcService iSysPlcService;

    /**
     * 订阅plc
     */
    @GetMapping(value = "/subscriptionPlc")
    public AjaxResult subscriptionPlc() throws Exception {
        return success(iSysPlcService.subscriptionPlc());
    }




}
