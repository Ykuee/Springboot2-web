package com.ykuee.datamaintenance.service.system.role.impl;

import java.util.List;

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
    
	@Override
	@Transactional
	public boolean addUserRole(List<SysUserRoleDTO> sysUserRoleList) {
		return saveBatch(sysUserRoleConverter.toEntity(sysUserRoleList));
	}

	@Override
	@Transactional
	public boolean delUserRole(List<SysUserRoleDTO> sysUserRoleList) {
		for (SysUserRoleDTO dto : sysUserRoleList) {
			LbuWrapper<SysUserRoleEntity> lbu = new LbuWrapper<SysUserRoleEntity>();
			//lbu.eq(SysUserRoleEntity::getId, dto.getId());
			lbu.eq(SysUserRoleEntity::getUserId, dto.getUserId());
			lbu.eq(SysUserRoleEntity::getRoleId, dto.getRoleId());
			remove(lbu);
			/*
			boolean success = remove(lbu);
			if (!success) {
				throw new BusinessException("用户删除权限时发生错误");
			}*/
		}
		return true;
	}

}
