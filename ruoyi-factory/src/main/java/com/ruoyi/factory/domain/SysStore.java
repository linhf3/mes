package com.ruoyi.factory.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 库存对象 sys_store
 *
 * @author linhognfei
 * @date 2024-12-26
 */
public class SysStore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库存id */
    private Long id;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String name;

    /** 物品编码 */
    @Excel(name = "物品编码")
    private String code;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    /** 安全库存量 */
    @Excel(name = "安全库存量")
    private Long safetyStock;

    /** 入库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入库日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date inData;

    /** 入库人姓名 */
    @Excel(name = "入库人姓名")
    private String inUserName;

    /** 入库人id */
    private Long inUserId;

    /** 出库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出库日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date outData;

    /** 出库人姓名 */
    @Excel(name = "出库人姓名")
    private String outUserName;

    /** 出库人id */
    private Long outUserId;

    /** 供应商id */
    private Long supplierId;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 库存状态 */
    @Excel(name = "库存状态")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setSpecification(String specification)
    {
        this.specification = specification;
    }

    public String getSpecification()
    {
        return specification;
    }
    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }

    public Long getQuantity()
    {
        return quantity;
    }
    public void setSafetyStock(Long safetyStock)
    {
        this.safetyStock = safetyStock;
    }

    public Long getSafetyStock()
    {
        return safetyStock;
    }
    public void setInData(Date inData)
    {
        this.inData = inData;
    }

    public Date getInData()
    {
        return inData;
    }
    public void setInUserName(String inUserName)
    {
        this.inUserName = inUserName;
    }

    public String getInUserName()
    {
        return inUserName;
    }
    public void setInUserId(Long inUserId)
    {
        this.inUserId = inUserId;
    }

    public Long getInUserId()
    {
        return inUserId;
    }
    public void setOutData(Date outData)
    {
        this.outData = outData;
    }

    public Date getOutData()
    {
        return outData;
    }
    public void setOutUserName(String outUserName)
    {
        this.outUserName = outUserName;
    }

    public String getOutUserName()
    {
        return outUserName;
    }
    public void setOutUserId(Long outUserId)
    {
        this.outUserId = outUserId;
    }

    public Long getOutUserId()
    {
        return outUserId;
    }
    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }
    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    public String getSupplierName()
    {
        return supplierName;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getRemarks()
    {
        return remarks;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("code", getCode())
                .append("specification", getSpecification())
                .append("quantity", getQuantity())
                .append("safetyStock", getSafetyStock())
                .append("inData", getInData())
                .append("inUserName", getInUserName())
                .append("inUserId", getInUserId())
                .append("outData", getOutData())
                .append("outUserName", getOutUserName())
                .append("outUserId", getOutUserId())
                .append("supplierId", getSupplierId())
                .append("supplierName", getSupplierName())
                .append("status", getStatus())
                .append("remarks", getRemarks())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
