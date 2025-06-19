package com.ruoyi.security.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.security.mapper.TbFluctuationLogMapper;
import com.ruoyi.security.domain.TbFluctuationLog;
import com.ruoyi.security.service.ITbFluctuationLogService;

/**
 * 波动日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-19
 */
@Service
public class TbFluctuationLogServiceImpl implements ITbFluctuationLogService 
{
    @Autowired
    private TbFluctuationLogMapper tbFluctuationLogMapper;

    /**
     * 查询波动日志
     * 
     * @param id 波动日志主键
     * @return 波动日志
     */
    @Override
    public TbFluctuationLog selectTbFluctuationLogById(Long id)
    {
        return tbFluctuationLogMapper.selectTbFluctuationLogById(id);
    }

    /**
     * 查询波动日志列表
     * 
     * @param tbFluctuationLog 波动日志
     * @return 波动日志
     */
    @Override
    public List<TbFluctuationLog> selectTbFluctuationLogList(TbFluctuationLog tbFluctuationLog)
    {
        return tbFluctuationLogMapper.selectTbFluctuationLogList(tbFluctuationLog);
    }

    /**
     * 新增波动日志
     * 
     * @param tbFluctuationLog 波动日志
     * @return 结果
     */
    @Override
    public int insertTbFluctuationLog(TbFluctuationLog tbFluctuationLog)
    {
        return tbFluctuationLogMapper.insertTbFluctuationLog(tbFluctuationLog);
    }

    /**
     * 修改波动日志
     * 
     * @param tbFluctuationLog 波动日志
     * @return 结果
     */
    @Override
    public int updateTbFluctuationLog(TbFluctuationLog tbFluctuationLog)
    {
        return tbFluctuationLogMapper.updateTbFluctuationLog(tbFluctuationLog);
    }

    /**
     * 批量删除波动日志
     * 
     * @param ids 需要删除的波动日志主键
     * @return 结果
     */
    @Override
    public int deleteTbFluctuationLogByIds(Long[] ids)
    {
        return tbFluctuationLogMapper.deleteTbFluctuationLogByIds(ids);
    }

    /**
     * 删除波动日志信息
     * 
     * @param id 波动日志主键
     * @return 结果
     */
    @Override
    public int deleteTbFluctuationLogById(Long id)
    {
        return tbFluctuationLogMapper.deleteTbFluctuationLogById(id);
    }

    @Override
    public int delateAllByLogType(int logType) {
        return tbFluctuationLogMapper.delateAllByLogType(logType);
    }

    @Override
    public int insertTbFluctuationLogList(List<TbFluctuationLog> record) {
        return tbFluctuationLogMapper.insertTbFluctuationLogList(record);
    }
}
