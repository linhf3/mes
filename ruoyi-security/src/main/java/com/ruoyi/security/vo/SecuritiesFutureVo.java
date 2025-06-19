package com.ruoyi.security.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class SecuritiesFutureVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 交易所-编码 */
    private String exchangeCode;

    /** 当前价格 */
    private Double price;

    /** 一日差价 */
    private String proportion;

    /** 振幅 */
    private Double dailySpread;

    /** 当前偏离值（比较上下偏离后得到） */
    private Double deviation;

    /** 波动 */
    private Double undulate;

    /** 提示状态 */
    private Integer positiveNegativeFlag;

    /** 当前振幅 */
    private Double theCurrentAmplitude;


}
