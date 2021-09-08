package com.ykuee.datamaintenance.service.system.role.impl;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.ykuee.datamaintenance.common.uidgenerator.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ykuee.datamaintenance.common.base.serviceImpl.ServiceImpl;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbuWrapper;
import com.ykuee.datamaintenance.mapper.system.role.SysUserRoleMapper;
import com.ykuee.datamaintenance.model.system.role.converter.SysUserRoleConverter;
import com.ykuee.datamaintenance.model.system.role.dto.SysUserRoleDTO;
import com.ykuee.datamaintenance.model.system.role.entity.SysUserRoleEntity;
import com.ykuee.datamaintenance.service.system.role.SysUserRoleService;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRoleEntity> implements SysUserRoleService {

	@Autowired
	private SysUserRoleConverter sysUserRoleConverter;

	@Autowired
	private IdGenerator<String> idGenerator;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addUserRole(List<SysUserRoleDTO> sysUserRoleList) {
		if(sysUserRoleList == null || sysUserRoleList.size() == 0){
			return true;
		}
		LbuWrapper<SysUserRoleEntity> lbu = new LbuWrapper<SysUserRoleEntity>();
		lbu.eq(SysUserRoleEntity::getUserId, sysUserRoleList.get(0).getUserId());
		remove(lbu);
		sysUserRoleList.forEach(dto -> dto.setId(idGenerator.generate()));
		return saveBatch(sysUserRoleConverter.toEntity(sysUserRoleList));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delUserRole(List<SysUserRoleDTO> sysUserRoleList) {
		for (SysUserRoleDTO dto : sysUserRoleList) {
			LbuWrapper<SysUserRoleEntity> lbu = new LbuWrapper<SysUserRoleEntity>();
			lbu.eq(SysUserRoleEntity::getUserId, dto.getUserId());
			lbu.eq(SysUserRoleEntity::getRoleId, dto.getRoleId());
			remove(lbu);
		}
		return true;
	}

	@Override
	public boolean delUserAllRoles(String userId) {
		if(StrUtil.isBlank(userId)){
			return true;
		}
		LbuWrapper<SysUserRoleEntity> lbu = new LbuWrapper<SysUserRoleEntity>();
		lbu.eq(SysUserRoleEntity::getUserId, userId);
		remove(lbu);
		return true;
	}

}
