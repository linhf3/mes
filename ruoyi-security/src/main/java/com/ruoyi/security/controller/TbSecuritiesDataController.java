package com.ruoyi.security.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.service.ITbSecuritiesDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 证劵交易数据源Controller
 * 
 * @author ruoyi
 * @date 2024-07-21
 */
@RestController
@RequestMapping("/security/futures")
public class TbSecuritiesDataController extends BaseController
{
    @Autowired
    private ITbSecuritiesDataService tbSecuritiesDataService;

    /**
     * 查询证劵交易数据源列表
     */
    @PreAuthorize("@ss.hasPermi('security:futures:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbSecuritiesData tbSecuritiesData)
    {
        startPage();
        List<TbSecuritiesData> list = tbSecuritiesDataService.selectTbSecuritiesDataList(tbSecuritiesData);
        return getDataTable(list);
    }

    /**
     * 导出证劵交易数据源列表
     */
    @PreAuthorize("@ss.hasPermi('security:futures:export')")
    @Log(title = "证劵交易数据源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbSecuritiesData tbSecuritiesData)
    {
        List<TbSecuritiesData> list = tbSecuritiesDataService.selectTbSecuritiesDataList(tbSecuritiesData);
        ExcelUtil<TbSecuritiesData> util = new ExcelUtil<TbSecuritiesData>(TbSecuritiesData.class);
        util.exportExcel(response, list, "证劵交易数据源数据");
    }

    /**
     * 获取证劵交易数据源详细信息
     */
    @PreAuthorize("@ss.hasPermi('security:futures:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbSecuritiesDataService.selectTbSecuritiesDataById(id));
    }

    /**
     * 新增证劵交易数据源
     */
    @PreAuthorize("@ss.hasPermi('security:futures:add')")
    @Log(title = "证劵交易数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbSecuritiesData tbSecuritiesData)
    {
        return toAjax(tbSecuritiesDataService.insertTbSecuritiesData(tbSecuritiesData));
    }

    /**
     * 修改证劵交易数据源
     */
    @PreAuthorize("@ss.hasPermi('security:futures:edit')")
    @Log(title = "证劵交易数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbSecuritiesData tbSecuritiesData)
    {
        return toAjax(tbSecuritiesDataService.updateTbSecuritiesData(tbSecuritiesData));
    }

    /**
     * 删除证劵交易数据源
     */
    @PreAuthorize("@ss.hasPermi('security:futures:remove')")
    @Log(title = "证劵交易数据源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbSecuritiesDataService.deleteTbSecuritiesDataByIds(ids));
    }

    /**
     * 爬取证劵交易数据
     */
    @Anonymous
    //@PreAuthorize("@ss.hasPermi('security:futures:crawl')")
    @Log(title = "证劵交易数据源", businessType = BusinessType.OTHER)
    @PostMapping("/crawl")
    public AjaxResult crawl()
    {
        return toAjax(tbSecuritiesDataService.crawl());
    }

    /**
     * 爬取证劵交易数据
     */
    @GetMapping("/lists")
    public TableDataInfo lists()
    {
        startPage();
        List<SecuritiesFutureVo> list = tbSecuritiesDataService.lists();
        return getDataTable(list);
    }

}
