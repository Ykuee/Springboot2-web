package com.ykuee.datamaintenance.common.base.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @version:
 * @Description: 基础实体
 * @author: Ykuee
 * @date: 2021-3-3 11:07:03
 * @param <T>
 */
public abstract class BaseEntity<T> extends AbstractEntity<T> {

	public static final String CREATED_BY = "createdBy";
	public static final String CREATED_DATE = "createdDate";
	public static final String UPDATED_BY = "updatedBy";
	public static final String UPDATED_DATE = "updatedDate";
	public static final String DEL_FLAG = "delFlag";
	
	@ApiModelProperty(value = "录入人ID")
	@TableField(value = "created_by", fill = FieldFill.INSERT)
	protected T createdBy;

	@ApiModelProperty(value = "录入时间")
	@TableField(value = "created_date", fill = FieldFill.INSERT)
	protected Date createdDate;
	

	@ApiModelProperty(value = "修改人ID")
	@TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
	protected T updatedBy;

	@ApiModelProperty(value = "修改时间")
	@TableField(value = "updated_date", fill = FieldFill.INSERT_UPDATE)
	protected Date updatedDate;

	@ApiModelProperty(value = "删除标志")
	@TableField(value = "del_flag", fill = FieldFill.INSERT)
	protected String delFlag;
	
	@Override
	public Object clone() {
		// 支持克隆 提高性能 仅仅是浅克隆
		return super.clone();
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
