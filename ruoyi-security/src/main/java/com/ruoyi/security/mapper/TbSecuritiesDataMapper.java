package com.ruoyi.security.mapper;

import java.util.List;
import com.ruoyi.security.domain.TbSecuritiesData;

/**
 * 证劵交易Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public interface TbSecuritiesDataMapper 
{
    /**
     * 查询证劵交易
     * 
     * @param id 证劵交易主键
     * @return 证劵交易
     */
    public TbSecuritiesData selectTbSecuritiesDataById(Long id);

    /**
     * 查询证劵交易列表
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 证劵交易集合
     */
    public List<TbSecuritiesData> selectTbSecuritiesDataList(TbSecuritiesData tbSecuritiesData);

    /**
     * 新增证劵交易
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 结果
     */
    public int insertTbSecuritiesData(TbSecuritiesData tbSecuritiesData);

    /**
     * 修改证劵交易
     * 
     * @param tbSecuritiesData 证劵交易
     * @return 结果
     */
    public int updateTbSecuritiesData(TbSecuritiesData tbSecuritiesData);

    /**
     * 删除证劵交易
     * 
     * @param id 证劵交易主键
     * @return 结果
     */
    public int deleteTbSecuritiesDataById(Long id);

    /**
     * 批量删除证劵交易
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbSecuritiesDataByIds(Long[] ids);

    /**
     * 批量删除证劵交易数据源
     *
     * @return 结果
     */
    public int deleteAll();

    /**
     * 批量新增证劵交易数据源
     *
     * @return 结果
     */
    int insertList(List<TbSecuritiesData> record);
}
