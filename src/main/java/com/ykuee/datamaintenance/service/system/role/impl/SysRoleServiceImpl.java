package com.ykuee.datamaintenance.service.system.role.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.base.serviceImpl.ServiceImpl;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbqWrapper;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbuWrapper;
import com.ykuee.datamaintenance.constant.YesOrNo;
import com.ykuee.datamaintenance.mapper.system.role.SysRoleMapper;
import com.ykuee.datamaintenance.model.system.role.converter.SysRoleConverter;
import com.ykuee.datamaintenance.model.system.role.dto.SysRoleDTO;
import com.ykuee.datamaintenance.model.system.role.entity.SysRoleEntity;
import com.ykuee.datamaintenance.service.system.role.SysRoleService;

import cn.hutool.core.util.StrUtil;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    
    @Autowired	
    private SysRoleConverter sysRoleConverter;
    

	@Override
	public List<SysRoleDTO> getSelectedRoleListByUserId(String userId) {
		List<SysRoleDTO> resList = sysRoleMapper.getRoleListByUserId(userId);
		return resList;
	}

	@Override
	public int getCountByCode(SysRoleDTO sysRoleDTO) {
		if(StrUtil.isBlank(sysRoleDTO.getCode())) {
			return 0;
		}
		LbqWrapper<SysRoleEntity> lbq = new LbqWrapper<SysRoleEntity>();
		lbq.ne(SysRoleEntity::getDelFlag, YesOrNo.NO.getKey());
		lbq.eq(SysRoleEntity::getCode, sysRoleDTO.getCode());
		lbq.ne(SysRoleEntity::getId, sysRoleDTO.getId());
		return count(lbq);
	}
	
	

	@Override
	@Transactional
	public boolean addRole(SysRoleDTO sysRoleDTO) {
		int count = getCountByCode(sysRoleDTO);
		if(count>0) {
			throw new BusinessException("已存在权限编码："+sysRoleDTO.getCode());
		}
		sysRoleDTO.setDisableFlag(YesOrNo.NO.getKey());
		return save(sysRoleConverter.toEntity(sysRoleDTO));
	}


	@Override
	@Transactional
	public boolean updateRole(SysRoleDTO sysRoleDTO) {
		int count = getCountByCode(sysRoleDTO);
		if(count>0) {
			throw new BusinessException("已存在权限编码："+sysRoleDTO.getCode());
		}
		return updateById(sysRoleConverter.toEntity(sysRoleDTO));
	}


	@Override
	@Transactional
	public boolean delRole(SysRoleDTO sysRoleDTO) {
		LbuWrapper<SysRoleEntity> lbu = new LbuWrapper<SysRoleEntity>();
		lbu.eq(SysRoleEntity::getId, sysRoleDTO.getId());
		lbu.eq(SysRoleEntity::getDelFlag, YesOrNo.YES.getKey());
		return update(lbu);
	}


	@Override
	public List<SysRoleDTO> getRolesByUserId(String userId) {
		return sysRoleMapper.getRolesByUserId(userId);
	}

}
