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
import com.ruoyi.security.domain.TbFluctuationLog;
import com.ruoyi.security.service.ITbFluctuationLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 波动日志Controller
 * 
 * @author ruoyi
 * @date 2025-03-19
 */
@RestController
@RequestMapping("/security/log")
public class TbFluctuationLogController extends BaseController
{
    @Autowired
    private ITbFluctuationLogService tbFluctuationLogService;

    /**
     * 查询波动日志列表
     */
    @PreAuthorize("@ss.hasPermi('security:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbFluctuationLog tbFluctuationLog)
    {
        startPage();
        List<TbFluctuationLog> list = tbFluctuationLogService.selectTbFluctuationLogList(tbFluctuationLog);
        return getDataTable(list);
    }

    /**
     * 导出波动日志列表
     */
    @PreAuthorize("@ss.hasPermi('security:log:export')")
    @Log(title = "波动日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbFluctuationLog tbFluctuationLog)
    {
        List<TbFluctuationLog> list = tbFluctuationLogService.selectTbFluctuationLogList(tbFluctuationLog);
        ExcelUtil<TbFluctuationLog> util = new ExcelUtil<TbFluctuationLog>(TbFluctuationLog.class);
        util.exportExcel(response, list, "波动日志数据");
    }

    /**
     * 获取波动日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('security:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbFluctuationLogService.selectTbFluctuationLogById(id));
    }

    /**
     * 新增波动日志
     */
    @PreAuthorize("@ss.hasPermi('security:log:add')")
    @Log(title = "波动日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbFluctuationLog tbFluctuationLog)
    {
        return toAjax(tbFluctuationLogService.insertTbFluctuationLog(tbFluctuationLog));
    }

    /**
     * 修改波动日志
     */
    @PreAuthorize("@ss.hasPermi('security:log:edit')")
    @Log(title = "波动日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbFluctuationLog tbFluctuationLog)
    {
        return toAjax(tbFluctuationLogService.updateTbFluctuationLog(tbFluctuationLog));
    }

    /**
     * 删除波动日志
     */
    @PreAuthorize("@ss.hasPermi('security:log:remove')")
    @Log(title = "波动日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbFluctuationLogService.deleteTbFluctuationLogByIds(ids));
    }
}
