package com.ruoyi.factory.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.factory.mapper.SysStoreMapper;
import com.ruoyi.factory.domain.SysStore;
import com.ruoyi.factory.service.ISysStoreService;

/**
 * 库存Service业务层处理
 * 
 * @author linhognfei
 * @date 2024-12-26
 */
@Service
public class SysStoreServiceImpl implements ISysStoreService 
{
    @Autowired
    private SysStoreMapper sysStoreMapper;

    /**
     * 查询库存
     * 
     * @param id 库存主键
     * @return 库存
     */
    @Override
    public SysStore selectSysStoreById(Long id)
    {
        return sysStoreMapper.selectSysStoreById(id);
    }

    /**
     * 查询库存列表
     * 
     * @param sysStore 库存
     * @return 库存
     */
    @Override
    public List<SysStore> selectSysStoreList(SysStore sysStore)
    {
        return sysStoreMapper.selectSysStoreList(sysStore);
    }

    /**
     * 新增库存
     * 
     * @param sysStore 库存
     * @return 结果
     */
    @Override
    public int insertSysStore(SysStore sysStore)
    {
        sysStore.setCreateTime(DateUtils.getNowDate());
        sysStore.setCreateTime(DateUtils.getNowDate());
        sysStore.setInData(DateUtils.getNowDate());
        sysStore.setInUserId(SecurityUtils.getUserId());
        sysStore.setCreateBy(SecurityUtils.getUsername());
        sysStore.setInUserName(SecurityUtils.getUsername());
        sysStore.setDelFlag("0");
        return sysStoreMapper.insertSysStore(sysStore);
    }

    /**
     * 修改库存
     * 
     * @param sysStore 库存
     * @return 结果
     */
    @Override
    public int updateSysStore(SysStore sysStore)
    {
        sysStore.setUpdateTime(DateUtils.getNowDate());
        sysStore.setUpdateBy(SecurityUtils.getUsername());
        return sysStoreMapper.updateSysStore(sysStore);
    }

    /**
     * 批量删除库存
     * 
     * @param ids 需要删除的库存主键
     * @return 结果
     */
    @Override
    public int deleteSysStoreByIds(Long[] ids)
    {
        return sysStoreMapper.deleteSysStoreByIds(ids);
    }

    /**
     * 删除库存信息
     * 
     * @param id 库存主键
     * @return 结果
     */
    @Override
    public int deleteSysStoreById(Long id)
    {
        return sysStoreMapper.deleteSysStoreById(id);
    }
}
