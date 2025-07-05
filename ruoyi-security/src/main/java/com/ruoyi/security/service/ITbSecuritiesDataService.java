package com.ruoyi.security.service;

import java.util.List;
import com.ruoyi.security.domain.TbSecuritiesData;
import com.ruoyi.security.vo.SecuritiesFutureVo;
import com.ruoyi.security.vo.SecuritiesSinaFutureVo;

/**
 * 证劵交易Service接口
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public interface ITbSecuritiesDataService 
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
     * 批量删除证劵交易
     * 
     * @param ids 需要删除的证劵交易主键集合
     * @return 结果
     */
    public int deleteTbSecuritiesDataByIds(Long[] ids);

    /**
     * 删除证劵交易信息
     * 
     * @param id 证劵交易主键
     * @return 结果
     */
    public int deleteTbSecuritiesDataById(Long id);

    /**
     * 爬取证劵交易数据
     */
    boolean crawl();

    /**
     * 爬取证劵交易数据
     */
    List<SecuritiesFutureVo> lists();

    /**
     * 查询实时期货分析数据
     */
    List<SecuritiesFutureVo> findList();

    List<SecuritiesSinaFutureVo> findSinaList();

    void updateFluctuationLog();

    List<SecuritiesSinaFutureVo> findSinaFiveList();

    void synSinaFiveData();

    List<SecuritiesSinaFutureVo> findDongFangFiveList() throws Exception;


    void logSina15(String flag) throws InterruptedException;

}
