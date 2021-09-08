/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.ykuee.datamaintenance.common.base.approval;

import cn.hutool.core.util.StrUtil;
import com.ykuee.datamaintenance.common.base.beancopy.DtoEntityConverter;
import com.ykuee.datamaintenance.common.base.constant.LogConstant;
import com.ykuee.datamaintenance.common.base.constant.TransType;
import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.log.SysLog;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @program: DataMaintenance
 * @description:
 * @author: Ykuee
 * @create: 2021-08-03 13:26
 **/
public abstract class ApprovalController< D extends ApprovalBaseDTO, E extends ApprovalBaseEntity, S extends ApprovalService<D,E>>  {

    private final static Logger logger = LoggerFactory.getLogger(ApprovalController.class);

    @Autowired
    protected DtoEntityConverter<D, E> converter;

    @Autowired
    protected S service;

    public abstract TransType getTransType();

    @SysLog(
            description = "提交审批",
            operateType = LogConstant.LogOperateType.SUBMIT,
            operateTypeDescription = "提交审批"
    )
    @ApiOperation("审批提交")
    @PostMapping("/workbench/submit")
    public void submit(@RequestBody D approvalDTO){
        logger.info("业务类型：{}，id：{}，提交审批",getTransType(),approvalDTO.getId());
        checkApprovalData(approvalDTO);
        try {
            this.service.submit(approvalDTO,getTransType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("审批提交时发生异常");
        }
    }

    @SysLog(
            description = "审批同意",
            operateType = LogConstant.LogOperateType.CHECKAGREE,
            operateTypeDescription = "审批同意"
    )
    @ApiOperation("审批同意")
    @PostMapping("/workbench/agree")
    public void agree(@RequestBody D approvalDTO) throws BusinessException {
        logger.info("业务类型：{}，id：{}，审批同意",getTransType(),approvalDTO.getId());
        checkApprovalData(approvalDTO);
        try {
            this.service.agree(approvalDTO,getTransType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("审批同意时发生异常");
        }
    }

    @SysLog(
            description = "审批拒绝",
            operateType = LogConstant.LogOperateType.CHECKREJECT,
            operateTypeDescription = "审批拒绝"
    )
    @ApiOperation("审批拒绝")
    @PostMapping("/workbench/reject")
    public void reject(@RequestBody D approvalDTO) throws BusinessException {
        logger.info("业务类型：{}，id：{}，审批拒绝",getTransType(),approvalDTO.getId());
        checkApprovalData(approvalDTO);
        try {
            this.service.reject(approvalDTO,getTransType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("审批拒绝时发生异常");
        }
    }

    @SysLog(
            description = "批量审批提交",
            operateType = LogConstant.LogOperateType.SUBMIT,
            operateTypeDescription = "批量审批提交"
    )
    @ApiOperation("批量审批提交")
    @PostMapping("/workbench/submitBatch")
    public void submitBatch(@RequestBody List<D> approvalDTOList){
        try {
            for (D approvalDTO:approvalDTOList){
                this.service.submit(approvalDTO,getTransType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("审批提交时发生异常");
        }
    }


    @SysLog(
            description = "批量审批同意",
            operateType = LogConstant.LogOperateType.CHECKAGREE,
            operateTypeDescription = "批量审批同意"
    )
    @ApiOperation("批量审批同意")
    @PostMapping("/workbench/agreeBatch")
    public void agreeBatch(@RequestBody List<D> approvalDTOList) throws BusinessException {
        try {
            for (D approvalDTO:approvalDTOList) {
                this.service.agree(approvalDTO, getTransType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("审批同意时发生异常");
        }
    }


    @SysLog(
            description = "批量审批拒绝",
            operateType = LogConstant.LogOperateType.CHECKREJECT,
            operateTypeDescription = "批量审批拒绝"
    )
    @ApiOperation("批量审批拒绝")
    @PostMapping("/workbench/rejectBatch")
    public void rejectBatch(@RequestBody List<D> approvalDTOList) throws BusinessException {
        try {
            for (D approvalDTO:approvalDTOList) {
                this.service.reject(approvalDTO, getTransType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("审批拒绝时发生异常");
        }
    }


    @SysLog(
            description = "获取审批列表",
            operateType = LogConstant.LogOperateType.QUERY,
            operateTypeDescription = "获取审批列表"
    )
    @ApiOperation("获取审批列表")
    @PostMapping("/workbench/getApprovingData")
    public List<D> getApprovingData() throws BusinessException {
        try {
            return this.service.getApprovingData(getTransType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取待审批列表发生异常");
        }
    }

    @SysLog(
            description = "获取审批列表 JSON",
            operateType = LogConstant.LogOperateType.QUERY,
            operateTypeDescription = "获取审批列表 JSON"
    )
    @ApiOperation("获取审批列表 JSON")
    @PostMapping("/workbench/getApprovingJsonData")
    public List<String> getApprovingJsonData() throws BusinessException {
        try {
            return this.service.getApprovingJsonData(getTransType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取待审批列表发生异常");
        }
    }

    private void checkApprovalData(D approvalDTO) {
        if(approvalDTO == null || approvalDTO.getId() == null || StrUtil.isBlank(String.valueOf(approvalDTO.getId()))){
            throw new BusinessException("审批参数校验时发生异常：id不能为空");
        }
    }

}
