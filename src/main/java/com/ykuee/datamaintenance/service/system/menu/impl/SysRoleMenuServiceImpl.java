package com.ykuee.datamaintenance.service.system.menu.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ykuee.datamaintenance.common.base.serviceImpl.ServiceImpl;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbuWrapper;
import com.ykuee.datamaintenance.mapper.system.menu.SysRoleMenuMapper;
import com.ykuee.datamaintenance.model.system.menu.converter.SysRoleMenuConverter;
import com.ykuee.datamaintenance.model.system.menu.dto.SysRoleMenuDTO;
import com.ykuee.datamaintenance.model.system.menu.entity.SysRoleMenuEntity;
import com.ykuee.datamaintenance.service.system.menu.SysRoleMenuService;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuEntity> implements SysRoleMenuService {

	@Autowired
	private SysRoleMenuConverter sysRoleMenuConverter;
	
	@Override
	@Transactional
	public boolean addRoleMenu(List<SysRoleMenuDTO> roleMenuDTO) {
		return saveBatch(sysRoleMenuConverter.toEntity(roleMenuDTO));
	}

	@Override
	@Transactional
	public boolean delRoleMenu(List<SysRoleMenuDTO> roleMenuDTO) {
		for (SysRoleMenuDTO dto : roleMenuDTO) {
			LbuWrapper<SysRoleMenuEntity> lbu = new LbuWrapper<SysRoleMenuEntity>();
			//lbu.eq(SysRoleMenuEntity::getId, dto.getId());
			lbu.eq(SysRoleMenuEntity::getMenuId, dto.getMenuId());
			lbu.eq(SysRoleMenuEntity::getRoleId, dto.getRoleId());
			remove(lbu);
			/*
			boolean success = remove(lbu);
			if (!success) {
				throw new BusinessException("用户删除菜单发生错误");
			}*/
		}
		return true;
	}
}
