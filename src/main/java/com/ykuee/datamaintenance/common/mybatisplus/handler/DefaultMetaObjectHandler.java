package com.ykuee.datamaintenance.common.mybatisplus.handler;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;
import com.ykuee.datamaintenance.common.base.constant.Constant;
import com.ykuee.datamaintenance.common.base.model.entity.AbstractEntity;
import com.ykuee.datamaintenance.common.base.model.entity.BaseEntity;
import com.ykuee.datamaintenance.common.support.JwtUtil;
import com.ykuee.datamaintenance.common.uidgenerator.IdGenerator;

import cn.hutool.core.util.StrUtil;

/**
 * @description： MyBatis Plus 元数据处理类【 inputTime, modifyTime, inputUserId,
 * modifyUserId 等字段】
 * 
 * @date ：2020/5/12 18:08
 */
public class DefaultMetaObjectHandler implements MetaObjectHandler {

	private static Logger logger = LoggerFactory.getLogger(DefaultMetaObjectHandler.class);

	private static final String FIELD_ID = "id";

	@Autowired
	IdGenerator<String> idGenerator;

	private DbType dbType;

	private String logicNotDeleteValue;

    public DefaultMetaObjectHandler(String logicNotDeleteValue, IdGenerator<String> idGenerator, String dbType) {
        super();
        this.logicNotDeleteValue = logicNotDeleteValue;
        this.idGenerator = idGenerator;
        this.dbType = DbType.getDbType(dbType);
    }

	public DefaultMetaObjectHandler(String logicNotDeleteValue, String dbType) {
		super();
		this.logicNotDeleteValue = logicNotDeleteValue;
		this.dbType = DbType.getDbType(dbType);
	}

	@Override
	public void insertFill(MetaObject metaObject) {
		// 仅针对继承AbstractEntity的自动注入
		Object entity = metaObject.getOriginalObject();
		this.processAbstractEntity(entity, metaObject);
		this.processBaseEntity(entity, metaObject);
		this.processLogicDelete(entity, metaObject);
	}

	private void processAbstractEntity(Object entity, MetaObject metaObject) {
		if (entity instanceof AbstractEntity) {
			Object oldId = ((AbstractEntity) entity).getId();

			if (ObjectUtils.isEmpty(oldId)) {
				Object id = idGenerator.generate();
				if (String.class.equals(metaObject.getGetterType(FIELD_ID))) {
					this.setFieldValByName(FIELD_ID, String.valueOf(id), metaObject);
				} else {
					this.setFieldValByName(FIELD_ID, id, metaObject);
				}
			}
		}
	}

	private void processBaseEntity(Object entity, MetaObject metaObject) {
		if (entity instanceof BaseEntity) {
			BaseEntity baseEntity = (BaseEntity) entity;
			if (ObjectUtils.isEmpty(baseEntity.getCreatedDate())) {
				this.setFieldValByName(BaseEntity.CREATED_DATE, getCurrentDateTime(), metaObject);
			}
			if (ObjectUtils.isEmpty(baseEntity.getCreatedBy())) {
				if (String.class == metaObject.getGetterType(BaseEntity.CREATED_BY)) {
					this.setFieldValByName(BaseEntity.CREATED_BY, getUserId(), metaObject);
				} else if (Long.class == metaObject.getGetterType(BaseEntity.CREATED_BY)) {
					this.setFieldValByName(BaseEntity.CREATED_BY, Long.valueOf(getUserId()), metaObject);
				} else {
					this.setFieldValByName(BaseEntity.CREATED_BY, getUserId(), metaObject);
				}
			}
			if (ObjectUtils.isEmpty(baseEntity.getDelFlag())) {
				if (String.class == metaObject.getGetterType(BaseEntity.DEL_FLAG)) {
					this.setFieldValByName(BaseEntity.DEL_FLAG, "0", metaObject);
				} else if (Long.class == metaObject.getGetterType(BaseEntity.DEL_FLAG)) {
					this.setFieldValByName(BaseEntity.DEL_FLAG, 0, metaObject);
				} else {
					this.setFieldValByName(BaseEntity.DEL_FLAG, "0", metaObject);
				}
			}

			update(metaObject, baseEntity);
		}
	}

	private void processLogicDelete(Object entity, MetaObject metaObject) {
		if (entity != null) {
			Field[] fields = entity.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				TableLogic tableLogic = field.getAnnotation(TableLogic.class);
				if (tableLogic == null) {
					continue;
				}

				String logicNotDeleteValue = tableLogic.value();
				if (!StringUtils.hasLength(logicNotDeleteValue)) {
					logicNotDeleteValue = this.logicNotDeleteValue;
				}

				if (BaseCodeEnum.class.isAssignableFrom(field.getType())) {
					Class<BaseCodeEnum<?>> baseCodeEnumClass = (Class<BaseCodeEnum<?>>) field.getType();
					BaseCodeEnum<?>[] baseCodeEnums = baseCodeEnumClass.getEnumConstants();
					for (int j = 0; j < baseCodeEnums.length; j++) {
						BaseCodeEnum<?> baseCodeEnum = baseCodeEnums[j];
						if (logicNotDeleteValue.equals(baseCodeEnum.getKey().toString())) {
							this.setFieldValByName(field.getName(), baseCodeEnum, metaObject);
						}
					}
				}
			}
		}
	}

	private Object getCurrentDateTime() {
		// return DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		return new Date();
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		logger.debug("start update fill ....");
		if (metaObject.getOriginalObject() instanceof BaseEntity) {
			BaseEntity entity = (BaseEntity) metaObject.getOriginalObject();
			update(metaObject, entity);
		} else {
			Object et = metaObject.getValue(Constants.ENTITY);
			if (et != null && et instanceof BaseEntity) {
				BaseEntity entity = (BaseEntity) et;
				update(metaObject, entity, Constants.ENTITY + ".");
			}
		}
	}

	private void update(MetaObject metaObject, BaseEntity entity, String et) {
		if (ObjectUtils.isEmpty(entity.getUpdatedBy())) {
			if (String.class == metaObject.getGetterType(et + BaseEntity.UPDATED_BY)) {
				this.setFieldValByName(BaseEntity.UPDATED_BY, getUserId(), metaObject);
			} else if (Long.class == metaObject.getGetterType(et + BaseEntity.UPDATED_BY)) {
				this.setFieldValByName(BaseEntity.UPDATED_BY, Long.valueOf(getUserId()), metaObject);
			} else {
				this.setFieldValByName(BaseEntity.UPDATED_BY, getUserId(), metaObject);
			}
		}
		if (ObjectUtils.isEmpty(entity.getUpdatedDate())) {
			this.setFieldValByName(BaseEntity.UPDATED_DATE, getCurrentDateTime(), metaObject);
		}
	}

	private void update(MetaObject metaObject, BaseEntity entity) {
		update(metaObject, entity, "");
	}

	private String getUserId() {
		String userId;
    	try {
    		String token = SecurityUtils.getSubject().getPrincipal().toString();
    		userId = JwtUtil.getClaim(token, Constant.USER_ID);
		} catch (Exception e) {
			logger.error("获取用户token发生异常");
			//throw new BusinessException("用户信息已过期，请重新登陆");
			return "";
		}
    	if(StrUtil.isBlank(userId)) {
    		//throw new BusinessException("获取用户发生异常");
    		return "";
    	}
	return userId;
	}
}
