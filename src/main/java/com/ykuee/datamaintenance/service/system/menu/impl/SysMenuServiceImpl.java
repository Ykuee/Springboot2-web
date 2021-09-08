package com.ykuee.datamaintenance.service.system.menu.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.base.node.ForestNodeMerger;
import com.ykuee.datamaintenance.common.base.serviceImpl.ServiceImpl;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbqWrapper;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbuWrapper;
import com.ykuee.datamaintenance.constant.YesOrNo;
import com.ykuee.datamaintenance.mapper.system.menu.SysMenuMapper;
import com.ykuee.datamaintenance.model.system.menu.converter.SysMenuConverter;
import com.ykuee.datamaintenance.model.system.menu.dto.SysMenuDTO;
import com.ykuee.datamaintenance.model.system.menu.entity.SysMenuEntity;
import com.ykuee.datamaintenance.model.system.role.entity.SysUserRoleEntity;
import com.ykuee.datamaintenance.service.system.menu.SysMenuService;
import com.ykuee.datamaintenance.service.system.role.SysUserRoleService;

import cn.hutool.core.util.StrUtil;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuEntity> implements SysMenuService {

	private static final Integer LEVEL_TYPE_HEAD= 1;

	@Autowired
	private SysMenuMapper sysMenuMapper;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private SysMenuConverter sysMenuConverter;

	@Override
	public int getCountByCode(SysMenuDTO sysMenuDTO) {
		if(StrUtil.isBlank(sysMenuDTO.getCode())) {
			return 0;
		}
		LbqWrapper<SysMenuEntity> lbq = new LbqWrapper<SysMenuEntity>();
		lbq.eq(SysMenuEntity::getCode, sysMenuDTO.getCode());
		lbq.ne(SysMenuEntity::getId, sysMenuDTO.getId());
		lbq.eq(SysMenuEntity::getDelFlag, YesOrNo.NO.getKey());
		return count(lbq);
	}

	@Override
	public List<SysMenuDTO> getTreeByUser(String userId) {
		LbqWrapper<SysUserRoleEntity> userRoleLbq = new LbqWrapper<SysUserRoleEntity>();
		userRoleLbq.eq(SysUserRoleEntity::getUserId, userId);
		List<SysUserRoleEntity> userRoleList = sysUserRoleService.list(userRoleLbq);
		if (CollectionUtils.isEmpty(userRoleList)) {
			return Collections.emptyList();
		}
		List<SysMenuDTO> roleMenuList = listSelectedByRoleId(userRoleList.stream()
				.map(SysUserRoleEntity::getRoleId).collect(Collectors.toList()));

		List<SysMenuDTO> res = listToTree(roleMenuList);
		for(SysMenuDTO head:res) {
			if(head !=null && LEVEL_TYPE_HEAD.equals(head.getLevelType())) {
				SysMenuDTO node = (SysMenuDTO) head.getChildren().get(0);
				if(node!=null) {
					head.setRedirect(head.getName()+"/"+node.getName());
				}
			}
		}
		return res;
	}

	/**
	 * (non-Javadoc)
	 * <p>Title: getMenuListByUserId</p>
	 * <p>Description: shiro鉴权时通过用户ID查询所有权限</p>
	 * @author Ykuee
	 * @date 2021-3-22
	 * @param userId
	 * @return
	 * @see com.jy.datamaintenance.service.system.menu.SysMenuService#getMenuListByUserId(java.lang.String)
	 */
	@Override
	public List<SysMenuDTO> getMenuListByUserId(String userId) {
		return sysMenuMapper.getMenuListByUserId(userId);
	}

	@Override
	public List<SysMenuDTO> listSelectedByRoleId(List<String> roleIdList) {
		return sysMenuMapper.listSelectedByRoleId(roleIdList);
	}

	@Override
	public List<SysMenuDTO> listByRoleId(String roleId) {
		return sysMenuMapper.listByRoleId(roleId);
	}

	@Override
	public List<SysMenuDTO> getMenuList(SysMenuDTO menuDTO) {
		LbqWrapper<SysMenuEntity> lbq = new LbqWrapper<SysMenuEntity>();
		lbq.eq(SysMenuEntity::getCode, menuDTO.getCode());
		lbq.eq(SysMenuEntity::getTitle, menuDTO.getTitle());
		lbq.eq(SysMenuEntity::getPath, menuDTO.getPath());
		lbq.eq(SysMenuEntity::getComponent, menuDTO.getComponent());
		lbq.eq(SysMenuEntity::getLevelType, menuDTO.getLevelType());
		lbq.eq(SysMenuEntity::getParentId, menuDTO.getParentId());
		if(StrUtil.isNotBlank(menuDTO.getSearchCode())) {
			lbq.and(wrapper ->wrapper
					.like(SysMenuEntity::getCode,menuDTO.getSearchCode())
					.like(SysMenuEntity::getTitle,menuDTO.getSearchCode())
					.like(SysMenuEntity::getPath,menuDTO.getSearchCode())
					.like(SysMenuEntity::getComponent,menuDTO.getSearchCode()));
		}
		return sysMenuConverter.toDto(list(lbq));
	}

	@Override
	public List<SysMenuDTO> getMenuTree(SysMenuDTO menuDTO) {
		return listToTree(getMenuList(menuDTO));
	}

	@Override
	public boolean addMenu(SysMenuDTO menuDTO) {
		int count = getCountByCode(menuDTO);
		if(count>0) {
			throw new BusinessException("已存在菜单编码："+menuDTO.getCode());
		}
		if(StrUtil.isBlank(menuDTO.getParentId())) {
			menuDTO.setParentId("0");
		}
		menuDTO.setDisableFlag(YesOrNo.NO.getKey());
		return save(sysMenuConverter.toEntity(menuDTO));
	}

	@Override
	public boolean delMenu(SysMenuDTO sysMenuDTO) {
		LbuWrapper<SysMenuEntity> lbq = new LbuWrapper<SysMenuEntity>();
		lbq.and(wrapper -> wrapper
				.eq(SysMenuEntity::getId, sysMenuDTO.getId())
				.or()
				.eq(SysMenuEntity::getParentId,sysMenuDTO.getId()));
		lbq.eq(SysMenuEntity::getDelFlag, YesOrNo.YES.getKey());
		return update(lbq);
	}

	@Override
	public boolean updateMenu(SysMenuDTO menuDTO) {
		int count = getCountByCode(menuDTO);
		if(count>0) {
			throw new BusinessException("已存在菜单编码："+menuDTO.getCode());
		}
		SysMenuEntity entity = sysMenuConverter.toEntity(menuDTO);
		return updateById(entity);
	}

	private List<SysMenuDTO> listToTree(List<SysMenuDTO> roleMenuList) {
		Map<String, SysMenuDTO> menuMap = roleMenuList.stream().collect(Collectors.toMap(SysMenuDTO::getId, a -> a,(k1,k2)->k1));
		// 查询父级
		for (SysMenuDTO mv: roleMenuList) {
			if(!LEVEL_TYPE_HEAD.equals(mv.getLevelType())){
				if(!menuMap.containsKey(mv.getParentId())){
					menuMap.put(mv.getParentId(),sysMenuConverter.toDto(sysMenuMapper.selectById(mv.getParentId())));
				}
			}
		}
		roleMenuList = menuMap.values().stream().collect(Collectors.toList());
		//根据sort字段排序
		roleMenuList.sort(Comparator.comparing(SysMenuDTO::getSort));
		List<SysMenuDTO> res = ForestNodeMerger.merge(roleMenuList);
		return res;
	}

	@Override
	public List<SysMenuDTO> treeByRoleId(String roleId) {
		return listToTree(listByRoleId(roleId));
	}
}
