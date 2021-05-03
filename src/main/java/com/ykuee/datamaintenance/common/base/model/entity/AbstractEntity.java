package com.ykuee.datamaintenance.common.base.model.entity;

import java.io.Serializable;

import javax.validation.groups.Default;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModelProperty;

public abstract class AbstractEntity<T> implements Serializable, Cloneable {

    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    protected T id;

    @Override
    public Object clone() {
        //支持克隆  提高性能  仅仅是浅克隆
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return e;
        }
    }
    
    /**
     * 保存和缺省验证组
     */
    public interface Save extends Default {

    }

    /**
     * 更新和缺省验证组
     */
    public interface Update extends Default {

    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

}
