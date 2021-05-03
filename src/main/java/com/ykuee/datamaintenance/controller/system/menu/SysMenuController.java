package com.ykuee.datamaintenance.controller.system.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykuee.datamaintenance.common.base.constant.LogConstant;
import com.ykuee.datamaintenance.common.base.constant.LogTypeConstant;
import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.log.entity.SysLog;
import com.ykuee.datamaintenance.common.support.UserUtil;
import com.ykuee.datamaintenance.model.system.menu.dto.SysMenuDTO;
import com.ykuee.datamaintenance.model.system.menu.dto.SysRoleMenuDTO;
import com.ykuee.datamaintenance.service.system.menu.SysMenuService;
import com.ykuee.datamaintenance.service.system.menu.SysRoleMenuService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author heguangyue
 * @since 2020-07-25
 */
@RestController
@RequestMapping("/sysMenu")
@Api(tags = "菜单管理")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
    
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    

	@Autowired
	private UserUtil userUtil;
	
	
	@SysLog(
			description = "查询当前用户的菜单树", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "查询当前用户的菜单树"
			)
	@ApiOperation(value = "用户菜单", notes = "查询当前用户可访问菜单")
    @GetMapping("/tree")
    public List<SysMenuDTO> tree() {
    	String userId = userUtil.getUserId();
    	if(StrUtil.isBlank(userId)) {
    		throw new BusinessException("获取用户失败");
    	}
    	List<SysMenuDTO> list = sysMenuService.getTreeByUser(userId);
        return list;
    }
    
	@SysLog(
			description = "查询所有菜单列表", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "查询所有菜单列表"
			)
    @GetMapping("/getMenuList")
    public List<SysMenuDTO> getMenuList(SysMenuDTO menuDTO) {
        return sysMenuService.getMenuList(menuDTO);
    }
	
	@SysLog(
			description = "查询所有菜单列表，该权限已选择标记selected", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "查询所有菜单列表"
			)
	@GetMapping("/listByRoleId")
	public List<SysMenuDTO> listByRoleId(String roleId) {
		return sysMenuService.listByRoleId(roleId);
	}
	
	@SysLog(
			description = "查询所有菜单树，该权限已选择标记selected", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "查询所有菜单树"
			)
	@GetMapping("/treeByRoleId")
	public List<SysMenuDTO> treeByRoleId(String roleId) {
		return sysMenuService.treeByRoleId(roleId);
	}
	
	@SysLog(
			description = "查询所有菜单树", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "查询所有菜单列树"
			)
	@GetMapping("/getMenuTree")
	public List<SysMenuDTO> getMenuTree(SysMenuDTO menuDTO) {
		return sysMenuService.getMenuTree(menuDTO);
	}
	
	@SysLog(
			description = "添加菜单", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "添加菜单"
			)
    @PostMapping("/addMenu")
    public void addMenu(@RequestBody SysMenuDTO menuDTO) {
		verifyData(menuDTO);
        boolean success = sysMenuService.addMenu(menuDTO);
        if(!success) {
			throw new BusinessException("新增菜单时发生错误");
		}

	}
	@SysLog(
			description = "通过id删除菜单", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "通过id删除菜单"
			)
	@PostMapping("/delMenu")
	public void delMenu(@RequestBody SysMenuDTO menuDTO) {
		boolean success = sysMenuService.delMenu(menuDTO);
		if(!success) {
			throw new BusinessException("删除菜单时发生错误");
		}
	}
	
	@SysLog(
			description = "通过id更新菜单", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "通过id更新菜单"
			)
	@PostMapping("/updateMenu")
	public void updateMenu(@RequestBody SysMenuDTO menuDTO) {
		boolean success = sysMenuService.updateMenu(menuDTO);
		if(!success) {
			throw new BusinessException("更新菜单时发生错误");
		}
	}
	
	@SysLog(
			description = "为用户分配菜单", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "为用户分配菜单"
			)
	@PostMapping("/addRoleMenu")
	public void addRoleMenu(@RequestBody List<SysRoleMenuDTO> roleMenuList) {
		verifyData(roleMenuList);
		boolean success = sysRoleMenuService.addRoleMenu(roleMenuList);
		if(!success) {
			throw new BusinessException("添加菜单时发生错误");
		}
	}
	
	@SysLog(
			description = "为用户删除已分配菜单", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "为用户删除已分配菜单"
			)
	@PostMapping("/delRoleMenu")
	public void delRoleMenu(@RequestBody List<SysRoleMenuDTO> roleMenuList) {
		boolean success = sysRoleMenuService.delRoleMenu(roleMenuList);
		if(!success) {
			throw new BusinessException("删除菜单时发生错误");
		}
	}
	
	private void verifyData(List<SysRoleMenuDTO> sysRoleMenuDTOList) {
		for(SysRoleMenuDTO dto:sysRoleMenuDTOList) {
			if (StrUtil.isBlank(dto.getMenuId())) {
				throw new BusinessException("菜单不能为空");
			}
			if (StrUtil.isBlank(dto.getRoleId())) {
				throw new BusinessException("权限不能为空");
			}
		}
	}

	private void verifyData(SysMenuDTO dto) {
		if (StrUtil.isBlank(dto.getCode())) {
			throw new BusinessException("菜单编码不能为空");
		}
		if (StrUtil.isBlank(dto.getTitle())) {
			throw new BusinessException("菜单名不能为空");
		}
		if (StrUtil.isBlank(dto.getPath())) {
			throw new BusinessException("菜单路径不能为空");
		}
		if (StrUtil.isBlank(dto.getComponent())) {
			throw new BusinessException("菜单路由不能为空");
		}
		if (ObjectUtil.isNull(dto.getLevelType())) {
			throw new BusinessException("菜单等级不能为空");
		}
		if (ObjectUtil.isNull(dto.getSort())) {
			throw new BusinessException("菜单排序不能为空");
		}
	}
}
