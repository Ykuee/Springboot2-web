package com.ykuee.datamaintenance.service.system.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ykuee.datamaintenance.common.base.serviceImpl.ServiceImpl;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbqWrapper;
import com.ykuee.datamaintenance.constant.YesOrNo;
import com.ykuee.datamaintenance.mapper.system.user.SysUserMapper;
import com.ykuee.datamaintenance.model.system.user.converter.SysUserConverter;
import com.ykuee.datamaintenance.model.system.user.dto.SysUserDTO;
import com.ykuee.datamaintenance.model.system.user.entity.SysUserEntity;
import com.ykuee.datamaintenance.service.system.user.SysUserService;

import cn.hutool.core.util.StrUtil;

/**
 * @Description:与用户操作相关
 * @Author: hgy
 * @Date: Created in 2018-03-29
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity>  implements SysUserService{

	@Autowired
	private SysUserConverter userConverter;
	
    @Autowired
    private SysUserMapper userMapper;

    
	@Override
	public SysUserDTO getUser(SysUserDTO userDto) {
		LbqWrapper<SysUserEntity> lbq = new LbqWrapper<SysUserEntity>();
		lbq.eq(SysUserEntity::getId, userDto.getId());
		lbq.eq(SysUserEntity::getCode, userDto.getCode());
		lbq.eq(SysUserEntity::getLoginName, userDto.getId());
		lbq.eq(SysUserEntity::getName, userDto.getId());
		lbq.eq(SysUserEntity::getDelFlag, YesOrNo.NO.getKey());
		return userConverter.toDto(getOne(lbq));
	}


	/**
	 * (non-Javadoc)
	  * <p>Title: updateUser</p>
	  * <p>Description: </p>
	  * @author Ykuee
	  * @date 2021-3-18 
	  * @param userInfo
	  * @return
	  * @see com.ykuee.datamaintenance.service.system.user.SysUserService#updateUser(com.ykuee.datamaintenance.model.system.user.dto.SysUserDTO)
	 */
	@Override
	@Transactional
	public boolean updateUser(SysUserDTO userInfo) {
		SysUserEntity entity = userConverter.toEntity(userInfo);
		return updateById(entity);
	}


	@Override
	public List<SysUserDTO> gerUserList(SysUserDTO userDto) {
		LbqWrapper<SysUserEntity> lbq = new LbqWrapper<SysUserEntity>();
		lbq.eq(SysUserEntity::getId, userDto.getId());
		lbq.eq(SysUserEntity::getCode, userDto.getCode());
		lbq.eq(SysUserEntity::getLoginName, userDto.getId());
		lbq.eq(SysUserEntity::getName, userDto.getId());
		lbq.eq(SysUserEntity::getDelFlag, YesOrNo.NO.getKey());
		if(StrUtil.isNotBlank(userDto.getSearchCode())) {
			lbq.and(wrapper ->wrapper
					.like(SysUserEntity::getCode,userDto.getSearchCode())
					.like(SysUserEntity::getName,userDto.getSearchCode())
					.like(SysUserEntity::getLoginName,userDto.getSearchCode()));
		}
		return userConverter.toDto(list(lbq));
	}

}
