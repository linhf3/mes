package com.ruoyi.security.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 证劵交易对象 tb_securities_data
 * 
 * @author ruoyi
 * @date 2025-01-12
 */
public class TbSecuritiesData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 交易所-编码 */
    @Excel(name = "交易所-编码")
    private String exchangeCode;

    /** 偏离值 */
    @Excel(name = "偏离值")
    private Double deviation;

    /** 波动 */
    @Excel(name = "波动")
    private Double undulate;

    /** 类型 */
    @Excel(name = "类型")
    private Integer type;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 创建人 */
    private String addUser;

    /** 创建时间 */
    private Date addTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setExchangeCode(String exchangeCode) 
    {
        this.exchangeCode = exchangeCode;
    }

    public String getExchangeCode() 
    {
        return exchangeCode;
    }
    public void setDeviation(Double deviation)
    {
        this.deviation = deviation;
    }

    public Double getDeviation()
    {
        return deviation;
    }
    public void setUndulate(Double undulate)
    {
        this.undulate = undulate;
    }

    public Double getUndulate()
    {
        return undulate;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setAddUser(String addUser) 
    {
        this.addUser = addUser;
    }

    public String getAddUser() 
    {
        return addUser;
    }
    public void setAddTime(Date addTime) 
    {
        this.addTime = addTime;
    }

    public Date getAddTime() 
    {
        return addTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("exchangeCode", getExchangeCode())
            .append("deviation", getDeviation())
            .append("undulate", getUndulate())
            .append("type", getType())
            .append("status", getStatus())
            .append("addUser", getAddUser())
            .append("addTime", getAddTime())
            .toString();
    }
}
