package com.ykuee.datamaintenance.model.system.log.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.ToString;

/**
 * <p>
 * 系统操作日志
 * </p>
 */
@ToString
@TableName("SYS_LOG")
public class SysLogEntity {

    /**
     * 操作IP
     */
    private String requestIp;

    /**
     * 日志类型【成功、危险，失败】
     */
    private String operateResult;

    /**
     * 用户id
     */
    private Object userId;

    /**
     * 用户名
     */
    private String userLoginName;


    /**
     * 操作描述
     */
    private String description;

    /**
     * 类路径
     */
    private String clazz;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求地址
     */
    private String requestUri;

    /**
     * 请求类型
     */
    private String httpMethod;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 返回值
     */
    private String result;

    /**
     * 模块码
     */
    private String moduleCode;
    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 业务描述
     */
    private String bizTypeDescription;
    /**
     * 操作类型
     */
    private String operateType;
    /**
     * 操作描述
     */
    private String operateTypeDescription;

    /**
     * 异常描述
     */
    private String exDesc;

    /**
     * 异常详情信息
     */
    private String exDetail;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 执行时间
     */
    private Long executeTime;

    /**
     * 浏览器
     */
    private String ua;

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getOperateResult() {
        return operateResult;
    }

    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizTypeDescription() {
        return bizTypeDescription;
    }

    public void setBizTypeDescription(String bizTypeDescription) {
        this.bizTypeDescription = bizTypeDescription;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateTypeDescription() {
        return operateTypeDescription;
    }

    public void setOperateTypeDescription(String operateTypeDescription) {
        this.operateTypeDescription = operateTypeDescription;
    }

    public String getExDesc() {
        return exDesc;
    }

    public void setExDesc(String exDesc) {
        this.exDesc = exDesc;
    }

    public String getExDetail() {
        return exDetail;
    }

    public void setExDetail(String exDetail) {
        this.exDetail = exDetail;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }
}
