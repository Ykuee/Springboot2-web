package com.ykuee.datamaintenance.common.base.model.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class BaseDTO<T> implements Serializable, Cloneable {

    @ApiModelProperty(value = "主键")
    protected T id;

    @ApiModelProperty(value = "录入人ID")
    protected T createdBy;

    @ApiModelProperty(value = "录入时间")
    protected Date createdDate;

    @ApiModelProperty(value = "修改人ID")
    protected T updatedBy;

    @ApiModelProperty(value = "修改人时间")
    protected Date updatedDate;

    @ApiModelProperty(value = "删除标记")
    protected String delFlag;
    
    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

	public T getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(T createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public T getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(T updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
