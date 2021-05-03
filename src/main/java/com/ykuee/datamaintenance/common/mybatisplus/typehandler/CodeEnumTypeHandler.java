package com.ykuee.datamaintenance.common.mybatisplus.typehandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * 
  * @version:
  * @Description: 枚举转换器
  * @author: Ykuee
  * @date: 2021-3-3 13:25:57
  * @param <E>
 */
public class CodeEnumTypeHandler<E extends Enum> extends BaseTypeHandler<Enum> {

    private Class<E> enumType;
    private Class<?> typeArg;
    private E[] enums;
    private Map<Object, E> enumMap = new ConcurrentHashMap<>();
    private boolean isBaseCodeEnum = false;

    public CodeEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }

        this.enumType = type;
        this.enums = type.getEnumConstants();

        this.getTypeArg();
    }

    private void getTypeArg() {
        Type[] interfaces = this.enumType.getGenericInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Type interfaceType = interfaces[i];

            if (interfaceType instanceof ParameterizedType) {
                ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) interfaceType;
                Class<?> rawType = parameterizedType.getRawType();
                if (rawType == BaseCodeEnum.class) {
                    // 枚举的真实类型是否是BaseCodeEnum
                    this.typeArg = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    this.isBaseCodeEnum = true;
                    this.listToMap();
                }
            } else {
                if (interfaceType == BaseCodeEnum.class) {
                    throw new RuntimeException(this.enumType.getName() + "：[类型参数不存在！]");
                }
            }
        }
    }

    /**
     * 为查询做优化，把list转成map结构
     */
    private void listToMap() {
        for (int i = 0; i < this.enums.length; i++) {
            E e = this.enums[i];
            BaseCodeEnum baseCodeEnum = (BaseCodeEnum) e;
            enumMap.put(baseCodeEnum.getKey(), e);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Enum parameter, JdbcType jdbcType)
            throws SQLException {
        if (isBaseCodeEnum) {
            BaseCodeEnum bce = (BaseCodeEnum) parameter;
            String key = String.valueOf(bce.getKey());
            if (typeArg == Integer.class) {
                ps.setInt(i, Integer.valueOf(key));
            } else if (typeArg == String.class) {
                ps.setString(i, key);
            } else {
                throw new RuntimeException(typeArg.getName() + "枚举泛型暂不支持");
            }
        } else {
            ps.setInt(i, parameter.ordinal());
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getNullableResult(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getNullableResult(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getNullableResult(cs.getString(columnIndex));
    }

    private E getNullableResult(String key) {
        try {
            if (StringUtils.hasLength(key)) {
                return isBaseCodeEnum ? toCodeEnum(key) : toOrdinalEnum(key);
            }
            return null;
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Cannot convert " + key + " to " + enumType.getSimpleName() + " by value.", ex);
        }
    }

    private E toCodeEnum(String key) {
        if (typeArg == Integer.class) {
            return enumMap.get(Integer.valueOf(key));
        } else if (typeArg == String.class) {
            return enumMap.get(key);
        } else {
            throw new RuntimeException(typeArg.getName() + "枚举泛型暂不支持");
        }
    }

    private E toOrdinalEnum(String key) {
        return enums[Integer.valueOf(key)];
    }

}
