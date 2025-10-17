package com.ruoyi.security.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.security.service.ITbSecuritiesDataService;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import com.ruoyi.security.vo.FutureVo;
import com.ruoyi.security.vo.SecuritiesSinaFutureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/money")
public class MoneyController extends BaseController {

    @Autowired
    private ITbSecuritiesDataService tbSecuritiesDataService;

    @PostMapping("/findSinaList")
    public TableDataInfo findSinaList(){
        List<SecuritiesSinaFutureVo> list = tbSecuritiesDataService.findSinaList();
        return getDataTable(list);
    }

    @PostMapping("/findList")
    public TableDataInfo findList(){
        List<SecuritiesFutureVo> list = tbSecuritiesDataService.findList();
        return getDataTable(list);
    }

    @PostMapping("/findListByPoints")
    public TableDataInfo findListByPoints(){
        List<FutureVo> list = tbSecuritiesDataService.findListByPoints();
        return getDataTable(list);
    }

     @PostMapping("/updateFluctuationLog")
    public void updateFluctuationLog(){
        tbSecuritiesDataService.updateFluctuationLog();
    }

    @PostMapping("/findSinaFiveList")
    public TableDataInfo findSinaFiveList(){
        startPage();
        List<SecuritiesSinaFutureVo> list = tbSecuritiesDataService.findSinaFiveList();
        return getDataTable(list);
    }
    
    @PostMapping("/findDongFangFiveList")
    public TableDataInfo findDongFangFiveList() throws Exception {
        List<SecuritiesSinaFutureVo> list = tbSecuritiesDataService.findDongFangFiveList();
        return getDataTable(list);
    }

    @PostMapping("/logSina15")
    public void logSina15(@RequestParam String flag) throws Exception {
        tbSecuritiesDataService.logSina15(flag);
    }



}
