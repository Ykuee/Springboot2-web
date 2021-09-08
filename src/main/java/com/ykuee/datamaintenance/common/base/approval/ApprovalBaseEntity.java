package com.ykuee.datamaintenance.common.base.approval;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ykuee.datamaintenance.common.base.constant.ApproveStatus;
import com.ykuee.datamaintenance.common.base.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @version:
 * @Description: 基础实体
 * @author: Ykuee
 * @date: 2021-3-3 11:07:03
 * @param <T>
 */
public class ApprovalBaseEntity<T> extends BaseEntity<T> {

	public static final String STATUS = "status";

	@ApiModelProperty(value = "审批状态")
	@TableField(value = "status", fill = FieldFill.INSERT_UPDATE)
	protected ApproveStatus status;

	public ApproveStatus getStatus() {
		return status;
	}

	public void setStatus(ApproveStatus status) {
		this.status = status;
	}
}
