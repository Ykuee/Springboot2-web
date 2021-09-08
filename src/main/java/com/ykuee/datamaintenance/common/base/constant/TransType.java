package com.ykuee.datamaintenance.common.base.constant;

import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;

public enum TransType implements BaseCodeEnum<String> {
    TEST_TYPE("TEST","测试业务类型");

    private String key;
    private String value;

    private TransType(String key, String value){
        this.key = key;
        this.value = value;
    }



    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
