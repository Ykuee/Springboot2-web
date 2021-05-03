package com.ykuee.datamaintenance.common.support;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ykuee.datamaintenance.common.base.constant.Constant;
import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbqWrapper;
import com.ykuee.datamaintenance.constant.YesOrNo;
import com.ykuee.datamaintenance.model.system.user.converter.SysUserConverter;
import com.ykuee.datamaintenance.model.system.user.dto.SysUserDTO;
import com.ykuee.datamaintenance.model.system.user.entity.SysUserEntity;
import com.ykuee.datamaintenance.service.system.user.SysUserService;

import cn.hutool.core.util.StrUtil;

/**
 * 获取当前登录用户工具类
 *
 */
@Component
public class UserUtil {

	private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);
	
    private SysUserService userService;
	private SysUserConverter userConverter;
	
    @Autowired
    public UserUtil(SysUserService userService,SysUserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    /**
     * 
      *<p>Title: getUser</p>
      *<p>Description: 获取当前登录用户</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @return
     */
    public SysUserDTO getUser() {
    	String token = "";
    	try {
    		token = SecurityUtils.getSubject().getPrincipal().toString();
		} catch (Exception e) {
			logger.error("获取用户token发生异常");
			throw new BusinessException("用户信息已过期，请重新登陆");
		}
        // 解密获得LoginName
        String loginName = JwtUtil.getClaim(token, Constant.LOGIN_NAME);
        String userId = JwtUtil.getClaim(token, Constant.USER_ID);
        SysUserDTO userDto = new SysUserDTO();
        LbqWrapper<SysUserEntity> lbq = new LbqWrapper<SysUserEntity>();
    	lbq.eq(SysUserEntity::getId, userId);
    	lbq.eq(SysUserEntity::getLoginName, loginName);
    	lbq.eq(SysUserEntity::getDelFlag, YesOrNo.NO.getKey());
    	SysUserEntity entity = userService.getOne(lbq);
    	userDto = userConverter.toDto(entity);
        // 用户是否存在
        if (userDto == null) {
            throw new BusinessException("该帐号不存在");
        }
        return userDto;
    }

    /**
     * 
      *<p>Title: getUserId</p>
      *<p>Description: 获取当前登录用户Id</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @return
     */
    public String getUserId() {
    	 String userId;
    	try {
    		String token = SecurityUtils.getSubject().getPrincipal().toString();
    		userId = JwtUtil.getClaim(token, Constant.USER_ID);
		} catch (Exception e) {
			logger.error("获取用户token发生异常");
			//throw new BusinessException("用户信息已过期，请重新登陆");
			return "";
		}
    	if(StrUtil.isBlank(userId)) {
    		//throw new BusinessException("获取用户ID发生异常");
    		return "";
    	}
        //解密获得getLoginName
        return userId;
    }

    /**
     * 
      *<p>Title: getToken</p>
      *<p>Description: 获取当前登录用户Token</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @return
     */
    public String getToken() {
    	String token;
    	try {
    		token = SecurityUtils.getSubject().getPrincipal().toString();
		} catch (Exception e) {
			logger.error("获取用户token发生异常");
			//throw new BusinessException("用户信息已过期，请重新登陆");
			return "";
		}
    	if(StrUtil.isBlank(token)) {
    		//throw new BusinessException("获取用户token发生异常");
    		return "";
    	}
        return token;
    }

    /**
     * 
      *<p>Title: getLoginName</p>
      *<p>Description: 获取当前登录用户LoginName</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @return
     */
    public String getLoginName() {
    	String loginName;
    	try {
    		String token = SecurityUtils.getSubject().getPrincipal().toString();
    		// 解密获得getLoginName
    		loginName = JwtUtil.getClaim(token, Constant.LOGIN_NAME);
		} catch (Exception e) {
			logger.error("获取用户token发生异常");
			//throw new BusinessException("用户信息已过期，请重新登陆");
			return "";
		}
    	if(StrUtil.isBlank(loginName)) {
    		//throw new BusinessException("获取用户loginName发生异常");
    		return "";
    	}
        return loginName;
    }
}
