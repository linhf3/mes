package com.ruoyi.security.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.security.mapper.TbSinaFifteenMapper;
import com.ruoyi.security.domain.TbSinaFifteen;
import com.ruoyi.security.service.ITbSinaFifteenService;

/**
 * 新浪15分钟日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-29
 */
@Service
public class TbSinaFifteenServiceImpl implements ITbSinaFifteenService 
{
    @Autowired
    private TbSinaFifteenMapper tbSinaFifteenMapper;

    /**
     * 查询新浪15分钟日志
     * 
     * @param id 新浪15分钟日志主键
     * @return 新浪15分钟日志
     */
    @Override
    public TbSinaFifteen selectTbSinaFifteenById(Long id)
    {
        return tbSinaFifteenMapper.selectTbSinaFifteenById(id);
    }

    /**
     * 查询新浪15分钟日志列表
     * 
     * @param tbSinaFifteen 新浪15分钟日志
     * @return 新浪15分钟日志
     */
    @Override
    public List<TbSinaFifteen> selectTbSinaFifteenList(TbSinaFifteen tbSinaFifteen)
    {
        return tbSinaFifteenMapper.selectTbSinaFifteenList(tbSinaFifteen);
    }

    /**
     * 新增新浪15分钟日志
     * 
     * @param tbSinaFifteen 新浪15分钟日志
     * @return 结果
     */
    @Override
    public int insertTbSinaFifteen(TbSinaFifteen tbSinaFifteen)
    {
        return tbSinaFifteenMapper.insertTbSinaFifteen(tbSinaFifteen);
    }

    /**
     * 修改新浪15分钟日志
     * 
     * @param tbSinaFifteen 新浪15分钟日志
     * @return 结果
     */
    @Override
    public int updateTbSinaFifteen(TbSinaFifteen tbSinaFifteen)
    {
        return tbSinaFifteenMapper.updateTbSinaFifteen(tbSinaFifteen);
    }

    /**
     * 批量删除新浪15分钟日志
     * 
     * @param ids 需要删除的新浪15分钟日志主键
     * @return 结果
     */
    @Override
    public int deleteTbSinaFifteenByIds(Long[] ids)
    {
        return tbSinaFifteenMapper.deleteTbSinaFifteenByIds(ids);
    }

    /**
     * 删除新浪15分钟日志信息
     * 
     * @param id 新浪15分钟日志主键
     * @return 结果
     */
    @Override
    public int deleteTbSinaFifteenById(Long id)
    {
        return tbSinaFifteenMapper.deleteTbSinaFifteenById(id);
    }
}
