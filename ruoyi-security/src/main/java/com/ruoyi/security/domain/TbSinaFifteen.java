package com.ruoyi.security.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 新浪15分钟日志对象 tb_sina_fifteen
 * 
 * @author ruoyi
 * @date 2025-06-29
 */
public class TbSinaFifteen extends BaseEntity
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

    /** 振幅 */
    @Excel(name = "振幅")
    private Double points;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date logDate;

    /** 1:期货，2：股票，3：外汇，4：btb */
    @Excel(name = "1:期货，2：股票，3：外汇，4：btb")
    private String reason;

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
    public void setPoints(Double points)
    {
        this.points = points;
    }

    public Double getPoints()
    {
        return points;
    }
    public void setLogDate(Date logDate) 
    {
        this.logDate = logDate;
    }

    public Date getLogDate() 
    {
        return logDate;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("points", getPoints())
            .append("logDate", getLogDate())
            .append("reason", getReason())
            .toString();
    }
}
