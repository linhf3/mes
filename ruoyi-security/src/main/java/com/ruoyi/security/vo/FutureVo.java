package com.ruoyi.security.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class FutureVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 名称 */
    private String name;

    /** 当前价格 */
    private Double price;

    /** 偏离 */
    private String proportion;

    /** 排名 */
    private Integer num = 0;

    /** 当前振幅 */
    private Double theCurrentAmplitude;

    /** 振幅 */
    private Double dailySpread;

    /** 前5振幅 */
    private String dailySpread5;


}
