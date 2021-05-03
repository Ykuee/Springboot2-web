package com.ykuee.datamaintenance.controller.system.role;


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
import com.ykuee.datamaintenance.model.system.role.dto.SysRoleDTO;
import com.ykuee.datamaintenance.model.system.role.dto.SysUserRoleDTO;
import com.ykuee.datamaintenance.service.system.role.SysRoleService;
import com.ykuee.datamaintenance.service.system.role.SysUserRoleService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author heguangyue
 * @since 2020-08-08
 */
@RestController
@RequestMapping("/sysRole")
@Api(tags = "角色管理")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    
    @Autowired
    private SysUserRoleService sysUserRoleService;
    
	@Autowired
	private UserUtil userUtil;
    
	@SysLog(
			description = "通过当前用户id查询所有角色", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.QUERY, 
			operateTypeDescription = "角色列表查询"
			)
	@ApiOperation(value = "角色列表查询", notes = "角色列表查询")
    @GetMapping("/getCurrentUserRoles")
    public List<SysRoleDTO> getCurrentUserRoles()  {
    	String userId = userUtil.getUserId();
        return sysRoleService.getSelectedRoleListByUserId(userId);
    }
    
    @ApiOperation(value = "角色关联列表查询", notes = "角色关联列表查询 已关联标记selected")
    @SysLog(
    		description = "通过用户id查询所有角色 已关联标记selected", 
    		bizType = LogTypeConstant.SYSTEMSERVICE, 
    		bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
    		operateType = LogConstant.LogOperateType.QUERY, 
    		operateTypeDescription = "角色列表查询"
    		)
    @GetMapping("/getRolesByUserId")
    public List<SysRoleDTO> getRolesByUserId(String userId)  {
    	return sysRoleService.getRolesByUserId(userId);
    }
    
	@SysLog(
			description = "新增权限", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.ADD, 
			operateTypeDescription = "新增权限"
			)
    @PostMapping("/addRole")
    public void addRole(@RequestBody SysRoleDTO sysRoleDTO) {
    	verifyData(sysRoleDTO);
    	boolean success = sysRoleService.addRole(sysRoleDTO);
    	if(!success) {
    		throw new BusinessException("新增权限时发生错误");
    	}
    }
	
	@SysLog(
			description = "更新权限", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.UPDATE, 
			operateTypeDescription = "更新权限"
			)
	@PostMapping("/updateRole")
	public void updateRole(@RequestBody SysRoleDTO sysRoleDTO) {
		boolean success = sysRoleService.updateRole(sysRoleDTO);
		if(!success) {
			throw new BusinessException("新增权限时发生错误");
		}
	}
	
	@SysLog(
			description = "逻辑删除权限", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.DELETE, 
			operateTypeDescription = "逻辑删除权限"
			)
	@PostMapping("/delRole")
	public void delRole(@RequestBody SysRoleDTO sysRoleDTO) {
		boolean success = sysRoleService.delRole(sysRoleDTO);
		if(!success) {
			throw new BusinessException("新增权限时发生错误");
		}
	}
	
	@SysLog(
			description = "新增用户与权限关联", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.ADD, 
			operateTypeDescription = "新增用户与权限关联"
			)
    @PostMapping("/addUserRole")
    public void addUserRole(@RequestBody List<SysUserRoleDTO> sysUserRoleList) {
    	verifyData(sysUserRoleList);
    	boolean success = sysUserRoleService.addUserRole(sysUserRoleList);
    	if(!success) {
    		throw new BusinessException("新增用户权限时发生错误");
    	}
    }
	
	@SysLog(
			description = "删除用户与权限关联", 
			bizType = LogTypeConstant.SYSTEMSERVICE, 
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME, 
			operateType = LogConstant.LogOperateType.DELETE, 
			operateTypeDescription = "删除用户与权限关联"
			)
	@PostMapping("/delUserRole")
	public void delUserRole(@RequestBody List<SysUserRoleDTO> sysUserRoleList) {
		verifyData(sysUserRoleList);
		boolean success = sysUserRoleService.delUserRole(sysUserRoleList);
		if(!success) {
			throw new BusinessException("删除用户权限时发生错误");
		}
	}
    
	private void verifyData(List<SysUserRoleDTO> sysUserRoleList) {
		for(SysUserRoleDTO dto:sysUserRoleList) {
			if (StrUtil.isBlank(dto.getUserId())) {
				throw new BusinessException("用户不能为空");
			}
			if (StrUtil.isBlank(dto.getRoleId())) {
				throw new BusinessException("权限不能为空");
			}
		}
	}

	private void verifyData(SysRoleDTO dto) {
		if (StrUtil.isBlank(dto.getCode())) {
			throw new BusinessException("权限编码不能为空");
		}
		if (StrUtil.isBlank(dto.getName())) {
			throw new BusinessException("权限名不能为空");
		}
	}
	
}
