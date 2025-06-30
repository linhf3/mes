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
import com.ruoyi.security.domain.TbSinaFifteen;
import com.ruoyi.security.service.ITbSinaFifteenService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 新浪15分钟日志Controller
 * 
 * @author ruoyi
 * @date 2025-06-29
 */
@RestController
@RequestMapping("/security/fifteen")
public class TbSinaFifteenController extends BaseController
{
    @Autowired
    private ITbSinaFifteenService tbSinaFifteenService;

    /**
     * 查询新浪15分钟日志列表
     */
    @PreAuthorize("@ss.hasPermi('security:fifteen:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbSinaFifteen tbSinaFifteen)
    {
        startPage();
        List<TbSinaFifteen> list = tbSinaFifteenService.selectTbSinaFifteenList(tbSinaFifteen);
        return getDataTable(list);
    }

    /**
     * 导出新浪15分钟日志列表
     */
    @PreAuthorize("@ss.hasPermi('security:fifteen:export')")
    @Log(title = "新浪15分钟日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbSinaFifteen tbSinaFifteen)
    {
        List<TbSinaFifteen> list = tbSinaFifteenService.selectTbSinaFifteenList(tbSinaFifteen);
        ExcelUtil<TbSinaFifteen> util = new ExcelUtil<TbSinaFifteen>(TbSinaFifteen.class);
        util.exportExcel(response, list, "新浪15分钟日志数据");
    }

    /**
     * 获取新浪15分钟日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('security:fifteen:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbSinaFifteenService.selectTbSinaFifteenById(id));
    }

    /**
     * 新增新浪15分钟日志
     */
    @PreAuthorize("@ss.hasPermi('security:fifteen:add')")
    @Log(title = "新浪15分钟日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbSinaFifteen tbSinaFifteen)
    {
        return toAjax(tbSinaFifteenService.insertTbSinaFifteen(tbSinaFifteen));
    }

    /**
     * 修改新浪15分钟日志
     */
    @PreAuthorize("@ss.hasPermi('security:fifteen:edit')")
    @Log(title = "新浪15分钟日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbSinaFifteen tbSinaFifteen)
    {
        return toAjax(tbSinaFifteenService.updateTbSinaFifteen(tbSinaFifteen));
    }

    /**
     * 删除新浪15分钟日志
     */
    @PreAuthorize("@ss.hasPermi('security:fifteen:remove')")
    @Log(title = "新浪15分钟日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbSinaFifteenService.deleteTbSinaFifteenByIds(ids));
    }
}
