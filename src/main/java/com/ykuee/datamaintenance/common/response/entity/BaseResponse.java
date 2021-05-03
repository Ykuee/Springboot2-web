package com.ykuee.datamaintenance.common.response.entity;

import com.ykuee.datamaintenance.common.page.entity.Page;

import io.swagger.annotations.ApiModelProperty;

public class BaseResponse<T> {

    @ApiModelProperty(value = "消息对象")
    private String msg;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "响应代码")
    private String code;

    @ApiModelProperty(value = "响应时间戳")
    private long timestamp = System.currentTimeMillis();

    @ApiModelProperty(value = "分页对象")
    private Page page;

    @ApiModelProperty(value = "响应数据")
    private T data;

    public BaseResponse() {
    	
    }

    public BaseResponse(String code, String msg, String path) {
    	this.code = code;
        this.msg = msg;
        this.path = path;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Page getPage() {
        return page;
    }

    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setPage(Page page) {
        this.page = page;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
