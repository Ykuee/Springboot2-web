package com.ykuee.datamaintenance.common.shiro.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.ykuee.datamaintenance.common.base.constant.Constant;
import com.ykuee.datamaintenance.common.shiro.token.JwtToken;
import com.ykuee.datamaintenance.common.support.JwtUtil;
import com.ykuee.datamaintenance.common.support.RedisUtil;
import com.ykuee.datamaintenance.model.system.menu.dto.SysMenuDTO;
import com.ykuee.datamaintenance.model.system.role.dto.SysRoleDTO;
import com.ykuee.datamaintenance.service.system.menu.SysMenuService;
import com.ykuee.datamaintenance.service.system.role.SysRoleService;
import com.ykuee.datamaintenance.service.system.user.SysUserService;

import cn.hutool.core.util.StrUtil;

/**
 * @Service
  * @version:
  * @Description: 身份校验核心类
  * @author: Ykuee
  * @date: 2021-3-3 9:17:24
 */
public class UserShiroRealm extends AuthorizingRealm {
	
	private static Logger logger = LoggerFactory.getLogger(UserShiroRealm.class);
	
	@Autowired
	@Lazy
    private SysUserService userService;
	@Autowired
	@Lazy
	private SysRoleService roleService;
	@Autowired
	@Lazy
	private SysMenuService menuService;


    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String loginName = JwtUtil.getClaim(principalCollection.toString(), Constant.LOGIN_NAME);
        String userId = JwtUtil.getClaim(principalCollection.toString(), Constant.USER_ID);
        // 查询用户角色
        logger.debug("查询用户角色 userId:{},loginname:{}",userId,loginName);
        List<SysRoleDTO> roleDtos = roleService.getSelectedRoleListByUserId(userId);
        for (SysRoleDTO roleDto : roleDtos) {
            if (roleDto != null) {
                // 添加角色Code
            	logger.debug("用户角色 roleCode:{}",roleDto.getCode());
            	simpleAuthorizationInfo.addRole(roleDto.getCode());
                // 根据用户角色查询菜单权限
            }
        }
        List<SysMenuDTO> menuDtos = menuService.getMenuListByUserId(userId);
        for (SysMenuDTO menuDto : menuDtos) {
        	if (menuDto != null) {
        		// 添加权限
        		logger.debug("查询用户菜单 menuCode:{}",menuDto.getCode());
        		simpleAuthorizationInfo.addStringPermission(menuDto.getCode());
        	}
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得loginName，用于和数据库进行对比
        String loginName = JwtUtil.getClaim(token, Constant.LOGIN_NAME);
        String userId = JwtUtil.getClaim(token, Constant.USER_ID);
        logger.debug("登录的token为:{},loginName:{},userId:{}",token,loginName,userId);
        // 帐号为空
        if (StrUtil.isBlank(loginName)||StrUtil.isBlank(userId)) {
        	logger.debug("获取用户信息失败，Token中的登录信息为空");
            throw new AuthenticationException("获取用户信息失败，Token中的登录信息为空");
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtil.verify(token) && RedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + loginName)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = RedisUtil.getObject(Constant.PREFIX_SHIRO_REFRESH_TOKEN + loginName).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "UserShiroRealm");
            }
        }
        logger.debug("Token时间戳不一致或redis中不存在此token.loginname:{}",loginName);
        throw new AuthenticationException("Token已过期");
    }
}
