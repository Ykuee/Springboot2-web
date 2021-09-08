/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.ykuee.datamaintenance.model.approvalRecard;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ykuee.datamaintenance.common.base.constant.ApproveStatus;
import com.ykuee.datamaintenance.common.base.constant.TransType;
import lombok.Data;

import java.util.Date;

/**
 * @program: DataMaintenance
 * @description:
 * @author: Ykuee
 * @create: 2021-08-02 15:52
 **/
@Data
@TableName("APPROVAL_RECARD")
public class ApprovalRecardEntity  {

    private String id;

    private TransType transType;

    private String recardId;

    private String dtoData;

    private String entityData;

    private String dtoClass;

    private String entityClass;

    private ApproveStatus status;

    private String delFlag;

    private Date submitDate;

    private String submitBy;

    private Date approveDate;

    private String approveBy;

}
