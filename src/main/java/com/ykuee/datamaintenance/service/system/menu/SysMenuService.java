package com.ykuee.datamaintenance.service.system.menu;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykuee.datamaintenance.model.system.menu.dto.SysMenuDTO;
import com.ykuee.datamaintenance.model.system.menu.entity.SysMenuEntity;

public interface SysMenuService extends IService<SysMenuEntity> {

	List<SysMenuDTO> getMenuListByUserId(String userId);

	List<SysMenuDTO> listSelectedByRoleId(List<String> roleIdList);

	List<SysMenuDTO> getTreeByUser(String userId);

	List<SysMenuDTO> getMenuList(SysMenuDTO menuDTO);

	boolean addMenu(SysMenuDTO menuDTO);

	boolean delMenu(SysMenuDTO menuDTO);

	boolean updateMenu(SysMenuDTO menuDTO);

	List<SysMenuDTO> getMenuTree(SysMenuDTO menuDTO);

	List<SysMenuDTO> listByRoleId(String roleId);

	List<SysMenuDTO> treeByRoleId(String roleId);

	int getCountByCode(SysMenuDTO sysMenuDTO);
}
