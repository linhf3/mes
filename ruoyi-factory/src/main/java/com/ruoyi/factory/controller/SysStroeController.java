package com.ruoyi.factory.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.factory.domain.SysStroe;
import com.ruoyi.factory.service.ISysStroeService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 库存Controller
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
@Controller
@RequestMapping("/factory/stroe")
public class SysStroeController extends BaseController
{
    private String prefix = "factory/stroe";

    @Autowired
    private ISysStroeService sysStroeService;

    @RequiresPermissions("factory:stroe:view")
    @GetMapping()
    public String stroe()
    {
        return prefix + "/stroe";
    }

    /**
     * 查询库存列表
     */
    @RequiresPermissions("factory:stroe:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysStroe sysStroe)
    {
        startPage();
        List<SysStroe> list = sysStroeService.selectSysStroeList(sysStroe);
        return getDataTable(list);
    }

    /**
     * 导出库存列表
     */
    @RequiresPermissions("factory:stroe:export")
    @Log(title = "库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysStroe sysStroe)
    {
        List<SysStroe> list = sysStroeService.selectSysStroeList(sysStroe);
        ExcelUtil<SysStroe> util = new ExcelUtil<SysStroe>(SysStroe.class);
        return util.exportExcel(list, "库存数据");
    }

    /**
     * 新增库存
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存库存
     */
    @RequiresPermissions("factory:stroe:add")
    @Log(title = "库存", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysStroe sysStroe)
    {
        return toAjax(sysStroeService.insertSysStroe(sysStroe));
    }

    /**
     * 修改库存
     */
    @RequiresPermissions("factory:stroe:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysStroe sysStroe = sysStroeService.selectSysStroeById(id);
        mmap.put("sysStroe", sysStroe);
        return prefix + "/edit";
    }

    /**
     * 修改保存库存
     */
    @RequiresPermissions("factory:stroe:edit")
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysStroe sysStroe)
    {
        return toAjax(sysStroeService.updateSysStroe(sysStroe));
    }

    /**
     * 删除库存
     */
    @RequiresPermissions("factory:stroe:remove")
    @Log(title = "库存", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysStroeService.deleteSysStroeByIds(ids));
    }
}
