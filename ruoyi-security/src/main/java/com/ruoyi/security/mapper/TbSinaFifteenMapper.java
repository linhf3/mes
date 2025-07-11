package com.ruoyi.security.mapper;

import java.util.List;
import com.ruoyi.security.domain.TbSinaFifteen;

/**
 * 新浪15分钟日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-29
 */
public interface TbSinaFifteenMapper 
{
    /**
     * 查询新浪15分钟日志
     * 
     * @param id 新浪15分钟日志主键
     * @return 新浪15分钟日志
     */
    public TbSinaFifteen selectTbSinaFifteenById(Long id);

    /**
     * 查询新浪15分钟日志列表
     * 
     * @param tbSinaFifteen 新浪15分钟日志
     * @return 新浪15分钟日志集合
     */
    public List<TbSinaFifteen> selectTbSinaFifteenList(TbSinaFifteen tbSinaFifteen);

    /**
     * 新增新浪15分钟日志
     * 
     * @param tbSinaFifteen 新浪15分钟日志
     * @return 结果
     */
    public int insertTbSinaFifteen(TbSinaFifteen tbSinaFifteen);

    /**
     * 修改新浪15分钟日志
     * 
     * @param tbSinaFifteen 新浪15分钟日志
     * @return 结果
     */
    public int updateTbSinaFifteen(TbSinaFifteen tbSinaFifteen);

    /**
     * 删除新浪15分钟日志
     * 
     * @param id 新浪15分钟日志主键
     * @return 结果
     */
    public int deleteTbSinaFifteenById(Long id);

    /**
     * 批量删除新浪15分钟日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbSinaFifteenByIds(Long[] ids);
}
