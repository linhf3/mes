package com.ruoyi.security.service;

import java.util.List;
import com.ruoyi.security.domain.TbFluctuationLog;

/**
 * 波动日志Service接口
 * 
 * @author ruoyi
 * @date 2025-03-19
 */
public interface ITbFluctuationLogService 
{
    /**
     * 查询波动日志
     * 
     * @param id 波动日志主键
     * @return 波动日志
     */
    public TbFluctuationLog selectTbFluctuationLogById(Long id);

    /**
     * 查询波动日志列表
     * 
     * @param tbFluctuationLog 波动日志
     * @return 波动日志集合
     */
    public List<TbFluctuationLog> selectTbFluctuationLogList(TbFluctuationLog tbFluctuationLog);

    /**
     * 新增波动日志
     * 
     * @param tbFluctuationLog 波动日志
     * @return 结果
     */
    public int insertTbFluctuationLog(TbFluctuationLog tbFluctuationLog);

    /**
     * 修改波动日志
     * 
     * @param tbFluctuationLog 波动日志
     * @return 结果
     */
    public int updateTbFluctuationLog(TbFluctuationLog tbFluctuationLog);

    /**
     * 批量删除波动日志
     * 
     * @param ids 需要删除的波动日志主键集合
     * @return 结果
     */
    public int deleteTbFluctuationLogByIds(Long[] ids);

    /**
     * 删除波动日志信息
     * 
     * @param id 波动日志主键
     * @return 结果
     */
    public int deleteTbFluctuationLogById(Long id);

    int delateAllByLogType(int logType);

    /**
     * 批量新增波动日志
     *
     * @return 结果
     */
    int insertTbFluctuationLogList(List<TbFluctuationLog> record);
}
