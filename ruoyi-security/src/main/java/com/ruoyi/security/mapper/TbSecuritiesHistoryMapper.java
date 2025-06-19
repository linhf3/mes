package com.ruoyi.security.mapper;

import java.util.List;
import com.ruoyi.security.domain.TbSecuritiesHistory;

/**
 * 历史日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-27
 */
public interface TbSecuritiesHistoryMapper 
{
    /**
     * 查询历史日志
     * 
     * @param id 历史日志主键
     * @return 历史日志
     */
    public TbSecuritiesHistory selectTbSecuritiesHistoryById(Long id);

    /**
     * 查询历史日志列表
     * 
     * @param tbSecuritiesHistory 历史日志
     * @return 历史日志集合
     */
    public List<TbSecuritiesHistory> selectTbSecuritiesHistoryList(TbSecuritiesHistory tbSecuritiesHistory);

    /**
     * 新增历史日志
     * 
     * @param tbSecuritiesHistory 历史日志
     * @return 结果
     */
    public int insertTbSecuritiesHistory(TbSecuritiesHistory tbSecuritiesHistory);

    /**
     * 修改历史日志
     * 
     * @param tbSecuritiesHistory 历史日志
     * @return 结果
     */
    public int updateTbSecuritiesHistory(TbSecuritiesHistory tbSecuritiesHistory);

    /**
     * 删除历史日志
     * 
     * @param id 历史日志主键
     * @return 结果
     */
    public int deleteTbSecuritiesHistoryById(Long id);

    /**
     * 批量删除历史日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbSecuritiesHistoryByIds(Long[] ids);

    public int insertTbSecuritiesHistoryList(List<TbSecuritiesHistory> list);
}
