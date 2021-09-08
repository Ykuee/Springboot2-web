package com.ykuee.datamaintenance.common.base.approval;

import com.ykuee.datamaintenance.common.base.constant.ApproveStatus;
import com.ykuee.datamaintenance.common.base.model.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @version:
 * @Description: 基础实体
 * @author: Ykuee
 * @date: 2021-3-3 11:07:03
 * @param <T>
 */
public class ApprovalBaseDTO<T> extends BaseDTO<T> {


	@ApiModelProperty(value = "审批状态")
	protected ApproveStatus status;

	public ApproveStatus getStatus() {
		return status;
	}

	public void setStatus(ApproveStatus status) {
		this.status = status;
	}
}
