package com.ruoyi.factory.mapper;

import java.util.List;
import com.ruoyi.factory.domain.SysStroe;

/**
 * 库存Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
public interface SysStroeMapper 
{
    /**
     * 查询库存
     * 
     * @param id 库存主键
     * @return 库存
     */
    public SysStroe selectSysStroeById(Long id);

    /**
     * 查询库存列表
     * 
     * @param sysStroe 库存
     * @return 库存集合
     */
    public List<SysStroe> selectSysStroeList(SysStroe sysStroe);

    /**
     * 新增库存
     * 
     * @param sysStroe 库存
     * @return 结果
     */
    public int insertSysStroe(SysStroe sysStroe);

    /**
     * 修改库存
     * 
     * @param sysStroe 库存
     * @return 结果
     */
    public int updateSysStroe(SysStroe sysStroe);

    /**
     * 删除库存
     * 
     * @param id 库存主键
     * @return 结果
     */
    public int deleteSysStroeById(Long id);

    /**
     * 批量删除库存
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysStroeByIds(String[] ids);
}
