package com.ruoyi.security.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.security.domain.TbSecuritiesHistory;
import com.ruoyi.security.service.ITbSecuritiesHistoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 历史日志Controller
 * 
 * @author ruoyi
 * @date 2025-03-27
 */
@RestController
@RequestMapping("/security/history")
public class TbSecuritiesHistoryController extends BaseController
{
    @Autowired
    private ITbSecuritiesHistoryService tbSecuritiesHistoryService;

    /**
     * 查询历史日志列表
     */
    @PreAuthorize("@ss.hasPermi('security:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbSecuritiesHistory tbSecuritiesHistory)
    {
        startPage();
        List<TbSecuritiesHistory> list = tbSecuritiesHistoryService.selectTbSecuritiesHistoryList(tbSecuritiesHistory);
        return getDataTable(list);
    }

    /**
     * 导出历史日志列表
     */
    @PreAuthorize("@ss.hasPermi('security:history:export')")
    @Log(title = "历史日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbSecuritiesHistory tbSecuritiesHistory)
    {
        List<TbSecuritiesHistory> list = tbSecuritiesHistoryService.selectTbSecuritiesHistoryList(tbSecuritiesHistory);
        ExcelUtil<TbSecuritiesHistory> util = new ExcelUtil<TbSecuritiesHistory>(TbSecuritiesHistory.class);
        util.exportExcel(response, list, "历史日志数据");
    }

    /**
     * 获取历史日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('security:history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbSecuritiesHistoryService.selectTbSecuritiesHistoryById(id));
    }

    /**
     * 新增历史日志
     */
    @PreAuthorize("@ss.hasPermi('security:history:add')")
    @Log(title = "历史日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbSecuritiesHistory tbSecuritiesHistory)
    {
        return toAjax(tbSecuritiesHistoryService.insertTbSecuritiesHistory(tbSecuritiesHistory));
    }

    /**
     * 修改历史日志
     */
    @PreAuthorize("@ss.hasPermi('security:history:edit')")
    @Log(title = "历史日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbSecuritiesHistory tbSecuritiesHistory)
    {
        return toAjax(tbSecuritiesHistoryService.updateTbSecuritiesHistory(tbSecuritiesHistory));
    }

    /**
     * 删除历史日志
     */
    @PreAuthorize("@ss.hasPermi('security:history:remove')")
    @Log(title = "历史日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbSecuritiesHistoryService.deleteTbSecuritiesHistoryByIds(ids));
    }
}
