package com.ruoyi.security.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.security.mapper.TbSecuritiesHistoryMapper;
import com.ruoyi.security.domain.TbSecuritiesHistory;
import com.ruoyi.security.service.ITbSecuritiesHistoryService;

/**
 * 历史日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-27
 */
@Service
public class TbSecuritiesHistoryServiceImpl implements ITbSecuritiesHistoryService 
{
    @Autowired
    private TbSecuritiesHistoryMapper tbSecuritiesHistoryMapper;

    /**
     * 查询历史日志
     * 
     * @param id 历史日志主键
     * @return 历史日志
     */
    @Override
    public TbSecuritiesHistory selectTbSecuritiesHistoryById(Long id)
    {
        return tbSecuritiesHistoryMapper.selectTbSecuritiesHistoryById(id);
    }

    /**
     * 查询历史日志列表
     * 
     * @param tbSecuritiesHistory 历史日志
     * @return 历史日志
     */
    @Override
    public List<TbSecuritiesHistory> selectTbSecuritiesHistoryList(TbSecuritiesHistory tbSecuritiesHistory)
    {
        return tbSecuritiesHistoryMapper.selectTbSecuritiesHistoryList(tbSecuritiesHistory);
    }

    /**
     * 新增历史日志
     * 
     * @param tbSecuritiesHistory 历史日志
     * @return 结果
     */
    @Override
    public int insertTbSecuritiesHistory(TbSecuritiesHistory tbSecuritiesHistory)
    {
        return tbSecuritiesHistoryMapper.insertTbSecuritiesHistory(tbSecuritiesHistory);
    }

    /**
     * 修改历史日志
     * 
     * @param tbSecuritiesHistory 历史日志
     * @return 结果
     */
    @Override
    public int updateTbSecuritiesHistory(TbSecuritiesHistory tbSecuritiesHistory)
    {
        return tbSecuritiesHistoryMapper.updateTbSecuritiesHistory(tbSecuritiesHistory);
    }

    /**
     * 批量删除历史日志
     * 
     * @param ids 需要删除的历史日志主键
     * @return 结果
     */
    @Override
    public int deleteTbSecuritiesHistoryByIds(Long[] ids)
    {
        return tbSecuritiesHistoryMapper.deleteTbSecuritiesHistoryByIds(ids);
    }

    /**
     * 删除历史日志信息
     * 
     * @param id 历史日志主键
     * @return 结果
     */
    @Override
    public int deleteTbSecuritiesHistoryById(Long id)
    {
        return tbSecuritiesHistoryMapper.deleteTbSecuritiesHistoryById(id);
    }

    @Override
    public int insertTbSecuritiesHistoryList(List<TbSecuritiesHistory> list) {
        return tbSecuritiesHistoryMapper.insertTbSecuritiesHistoryList(list);
    }
}
