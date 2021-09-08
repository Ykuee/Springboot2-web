package com.ykuee.datamaintenance.service.system.menu.impl;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.ykuee.datamaintenance.common.uidgenerator.IdGenerator;
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

	@Autowired
	private IdGenerator<String> idGenerator;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addRoleMenu(List<SysRoleMenuDTO> roleMenuDTOList) {
		if(roleMenuDTOList == null || roleMenuDTOList.size() == 0){
			return true;
		}
		LbuWrapper<SysRoleMenuEntity> lbu = new LbuWrapper<SysRoleMenuEntity>();
		lbu.eq(SysRoleMenuEntity::getRoleId, roleMenuDTOList.get(0).getRoleId());
		remove(lbu);
		roleMenuDTOList.forEach(dto -> dto.setId(idGenerator.generate()));
		return saveBatch(sysRoleMenuConverter.toEntity(roleMenuDTOList));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delRoleMenu(List<SysRoleMenuDTO> roleMenuDTO) {
		for (SysRoleMenuDTO dto : roleMenuDTO) {
			LbuWrapper<SysRoleMenuEntity> lbu = new LbuWrapper<SysRoleMenuEntity>();
			lbu.eq(SysRoleMenuEntity::getMenuId, dto.getMenuId());
			lbu.eq(SysRoleMenuEntity::getRoleId, dto.getRoleId());
			remove(lbu);
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delRoleAllMenu(String roleId) {
		if(StrUtil.isBlank(roleId)){
			return true;
		}
		LbuWrapper<SysRoleMenuEntity> lbu = new LbuWrapper<SysRoleMenuEntity>();
		lbu.eq(SysRoleMenuEntity::getRoleId, roleId);
		remove(lbu);
		return true;
	}

}
