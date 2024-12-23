package com.ruoyi.factory.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.factory.mapper.SysStroeMapper;
import com.ruoyi.factory.domain.SysStroe;
import com.ruoyi.factory.service.ISysStroeService;
import com.ruoyi.common.core.text.Convert;

/**
 * 库存Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
@Service
public class SysStroeServiceImpl implements ISysStroeService 
{
    @Autowired
    private SysStroeMapper sysStroeMapper;

    /**
     * 查询库存
     * 
     * @param id 库存主键
     * @return 库存
     */
    @Override
    public SysStroe selectSysStroeById(Long id)
    {
        return sysStroeMapper.selectSysStroeById(id);
    }

    /**
     * 查询库存列表
     * 
     * @param sysStroe 库存
     * @return 库存
     */
    @Override
    public List<SysStroe> selectSysStroeList(SysStroe sysStroe)
    {
        return sysStroeMapper.selectSysStroeList(sysStroe);
    }

    /**
     * 新增库存
     * 
     * @param sysStroe 库存
     * @return 结果
     */
    @Override
    public int insertSysStroe(SysStroe sysStroe)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        sysStroe.setCreateTime(DateUtils.getNowDate());
        sysStroe.setInData(DateUtils.getNowDate());
        sysStroe.setInUserId(currentUser.getUserId());
        sysStroe.setCreateBy(currentUser.getUserName());
        sysStroe.setInUserName(currentUser.getUserName());
        sysStroe.setDelFlag("0");
        return sysStroeMapper.insertSysStroe(sysStroe);
    }

    /**
     * 修改库存
     * 
     * @param sysStroe 库存
     * @return 结果
     */
    @Override
    public int updateSysStroe(SysStroe sysStroe)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        sysStroe.setUpdateTime(DateUtils.getNowDate());
        sysStroe.setUpdateBy(currentUser.getUserName());
        return sysStroeMapper.updateSysStroe(sysStroe);
    }

    /**
     * 批量删除库存
     * 
     * @param ids 需要删除的库存主键
     * @return 结果
     */
    @Override
    public int deleteSysStroeByIds(String ids)
    {
        return sysStroeMapper.deleteSysStroeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除库存信息
     * 
     * @param id 库存主键
     * @return 结果
     */
    @Override
    public int deleteSysStroeById(Long id)
    {
        return sysStroeMapper.deleteSysStroeById(id);
    }
}
