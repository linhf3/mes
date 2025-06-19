package com.ruoyi.security.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class SecuritiesSinaFutureVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 当前价格 */
    private Double price;

    /**
     * 提示语
     */
    private String msg;

    /** 提示状态 */
    private Integer positiveNegativeFlag;

    /**
     * 日期，年月日时分秒
     */
    private String time;


}
