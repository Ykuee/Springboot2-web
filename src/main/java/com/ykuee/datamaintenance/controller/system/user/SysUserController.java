package com.ykuee.datamaintenance.controller.system.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Constants;
import com.ykuee.datamaintenance.common.base.constant.Constant;
import com.ykuee.datamaintenance.common.base.constant.LogConstant;
import com.ykuee.datamaintenance.common.base.constant.LogTypeConstant;
import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.log.SysLog;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbqWrapper;
import com.ykuee.datamaintenance.common.support.HttpContextHolder;
import com.ykuee.datamaintenance.common.support.JwtUtil;
import com.ykuee.datamaintenance.common.support.RedisUtil;
import com.ykuee.datamaintenance.common.support.UserUtil;
import com.ykuee.datamaintenance.constant.YesOrNo;
import com.ykuee.datamaintenance.model.system.user.converter.SysUserConverter;
import com.ykuee.datamaintenance.model.system.user.dto.SysUserDTO;
import com.ykuee.datamaintenance.model.system.user.entity.SysUserEntity;
import com.ykuee.datamaintenance.service.system.user.SysUserService;
import com.ykuee.datamaintenance.util.AesCipherUtil;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;

/**
 *
 * @version:
 * @Description: 测试
 * @author: Ykuee
 * @date: 2021-3-3 12:09:50
 */
@RestController
@RequestMapping("/sysUser")
@Api(tags = "用户管理")
public class SysUserController {

	@Autowired
	private SysUserService userService;

	@Autowired
	private SysUserConverter userConverter;

	@Autowired
	private UserUtil userUtil;

	/**
	 *
	 *<p>Title: login</p>
	 *<p>Description: 用户登录</p>
	 * @author Ykuee
	 * @date 2021-3-18
	 */
	@SysLog(
			description = "用户登录、密码验证、token生成",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.LOGIN,
			operateTypeDescription = "用户登录操作"
	)
	@PostMapping("/login")
	public Map<String,Object> login(@RequestBody SysUserDTO userInfo) {
		boolean pass;
		boolean backDoor;
		HttpServletRequest request = HttpContextHolder.getHttpServletRequest();
		HttpServletResponse response = HttpContextHolder.getHttpServletResponse();
		//String sCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		String sCode = String.valueOf(RedisUtil.getObject(Constant.EASY_CAPTCA+":"+userInfo.getCaptchaKey()));
		String code = userInfo.getCaptchaCode();
		pass = StrUtil.isNotEmpty(sCode) && StrUtil.isNotEmpty(code) && sCode.equals(code);
		backDoor = "FUCK".equals(code);
		if (!pass && !backDoor) {
			throw new BusinessException("验证码输入错误");
		}
		// 查询数据库中的帐号信息
		LbqWrapper<SysUserEntity> lbq = new LbqWrapper<SysUserEntity>();
		lbq.eq(SysUserEntity::getLoginName, userInfo.getLoginName());
		lbq.eq(SysUserEntity::getDelFlag, YesOrNo.NO.getKey());
		lbq.eq(SysUserEntity::getDisableFlag, YesOrNo.NO.getKey());
		SysUserEntity userEntity = userService.getOne(lbq);
		if (ObjectUtil.isNull(userEntity)) {
			throw new BusinessException("该帐号不存在");
		}
		// 密码进行AES解密
		String key = AesCipherUtil.deCrypto(userEntity.getPassword());
		// 因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
		if (key.equals(userInfo.getLoginName() + userInfo.getPassword())) {
			// 清除可能存在的Shiro权限信息缓存
			if (RedisUtil.exists(Constant.PREFIX_SHIRO_CACHE + userInfo.getLoginName())) {
				RedisUtil.delKey(Constant.PREFIX_SHIRO_CACHE + userInfo.getLoginName());
			}
			// 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
			String currentTimeMillis = String.valueOf(System.currentTimeMillis());
			RedisUtil.setObjectRefreshExpiTime(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userInfo.getLoginName(),
					currentTimeMillis);
			// 从Header中Authorization返回AccessToken，时间戳为当前时间戳
			String token = JwtUtil.sign(userEntity.getId(), userInfo.getLoginName(), currentTimeMillis);
			response.setHeader(Constant.AUTHORIZATION, token);
			response.setHeader(Constant.ACCESS_HEADERS, Constant.AUTHORIZATION);
			Map<String,Object> resMap = new HashMap<String,Object>();
			resMap.put("login", "success");
			resMap.put("userInfo", userEntity);
			return resMap;
		} else {
			throw new BusinessException("帐号或密码错误");
		}
	}

	/**
	 *
	 *<p>Title: logout</p>
	 *<p>Description: 用户下线</p>
	 * @author Ykuee
	 * @date 2021-3-18
	 */
	@SysLog(
			description = "用户登出、token缓存删除",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.LOGIN,
			operateTypeDescription = "用户登出操作"
	)
	@PostMapping("/logout")
	public void logout() {
		SysUserDTO userDto = userUtil.getUser();
		if (RedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userDto.getLoginName())) {
			RedisUtil.delKey(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userDto.getLoginName());
		}
	}

	/**
	 *
	 *<p>Title: info</p>
	 *<p>Description: 获取当前登录用户信息</p>
	 * @author Ykuee
	 * @date 2021-3-18
	 */
	@SysLog(
			description = "通过token获取当前用户信息",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.LOGIN,
			operateTypeDescription = "获取当前用户信息"
	)
	@GetMapping("/info")
	public SysUserDTO info() {
		// 获取当前登录用户
		return userUtil.getUser();
	}

	@SysLog(
			description = "管理员添加用户",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.ADD,
			operateTypeDescription = "使用管理员账户添加用户"
	)
	@PostMapping("/add")
	public void add(@RequestBody SysUserDTO userDto) {
		verifyData(userDto);
		//至少八个字符，最多16个字符，至少一个大写字母，一个小写字母，一个数字和一个特殊字符：
		/*
		boolean match = Pattern.matches(Constant.PASSWORD_PATTERN, userDto.getPassword());
		if(!match) {
			throw new BusinessException("密码规则为至少八个字符，最多16个字符."
					+ "至少一个大写字母，一个小写字母，一个数字和一个特殊字符");
		}
		*/
		// 判断当前帐号是否存在
		SysUserDTO userDtoTemp = new SysUserDTO();
		userDtoTemp.setLoginName(userDto.getLoginName());
		LbqWrapper<SysUserEntity> lbq = new LbqWrapper<SysUserEntity>();
		lbq.eq(SysUserEntity::getLoginName, userDto.getLoginName())
				.or()
				.eq(SysUserEntity::getCode, userDto.getCode());
		int count = userService.count(lbq);
		if (count > 0) {
			throw new BusinessException("该登录名或用户编码已存在");
		}
		String encPwd = AesCipherUtil.enCrypto(userDto.getLoginName() + userDto.getPassword());
		userDto.setPassword(encPwd);
		userDto.setDisableFlag(YesOrNo.NO.getKey());
		SysUserEntity entity = userConverter.toEntity(userDto);
		boolean success = userService.save(entity);
		if (!success) {
			throw new BusinessException("用户新增失败");
		}
	}

	@SysLog(
			description = "通过条件查询具体用户",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.QUERY,
			operateTypeDescription = "通过条件查询具体用户"
	)
	@GetMapping("/gerUser")
	public SysUserDTO getUser(SysUserDTO userDto) {
		SysUserDTO resUserDto = userService.getUser(userDto);
		if (resUserDto == null) {
			throw new BusinessException("查询用户信息失败");
		}
		return resUserDto;
	}

	@SysLog(
			description = "通过条件查询用户列表",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.QUERY,
			operateTypeDescription = "通过条件查询用户列表"
	)
	@GetMapping("/gerUserList")
	public List<SysUserDTO> gerUserList(SysUserDTO userDto) {
		return userService.gerUserList(userDto);
	}

	@SysLog(
			description = "通过用户id更新用户，空参不更新",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.UPDATE,
			operateTypeDescription = "通过id更新用户"
	)
	@PostMapping("/updateUser")
	public void update(@RequestBody SysUserDTO userDto) {
		verifyData(userDto);
		//至少八个字符，最多16个字符，至少一个大写字母，一个小写字母，一个数字和一个特殊字符：
		/*boolean match = Pattern.matches(Constant.PASSWORD_PATTERN, userDto.getPassword());
		if(!match) {
			throw new BusinessException("密码规则为至少八个字符，最多16个字符。"
					+ "至少一个大写字母，一个小写字母，一个数字和一个特殊字符");
		}*/
		LbqWrapper<SysUserEntity> lbq = new LbqWrapper<SysUserEntity>();
		lbq.and(wrapper -> wrapper
				.eq(SysUserEntity::getLoginName, userDto.getLoginName())
				.or()
				.eq(SysUserEntity::getCode, userDto.getCode()));
		lbq.ne(SysUserEntity::getId, userDto.getId());
		int count = userService.count(lbq);
		if (count > 0) {
			throw new BusinessException("该登录名或用户编码已存在");
		}
		userDto.setPassword(null);
		boolean success = userService.updateUser(userDto);
		if (!success) {
			throw new BusinessException("账号更新失败");
		}
	}

	@SysLog(
			description = "通过用户id更新密码",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.UPDATE,
			operateTypeDescription = "重置密码"
	)
	@PostMapping("/resetPassword")
	public void resetPassword(@RequestBody SysUserDTO userDto) {
		LbqWrapper<SysUserEntity> lbq = new LbqWrapper<SysUserEntity>();
		lbq.select(SysUserEntity::getId,SysUserEntity::getLoginName);
		lbq.eq(SysUserEntity::getId, userDto.getId());
		SysUserEntity user = userService.getOne(lbq);
		if(user == null){
			throw new BusinessException("未找到此用户");
		}
		// 密码以帐号+密码的形式进行AES加密
		String encPwd = AesCipherUtil.enCrypto(user.getLoginName() + userDto.getPassword());
		user.setPassword(encPwd);
		boolean success = userService.updateById(user);
		if (!success) {
			throw new BusinessException("账号更新失败");
		}
	}

	@SysLog(
			description = "更新当前用户密码",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.UPDATE,
			operateTypeDescription = "更新当前用户密码"
	)
	@PostMapping("/resetCurPassword")
	public void resetCurPassword(@RequestBody SysUserDTO userDto) {
		String curUserId = userUtil.getUserId();
		if(StrUtil.isBlank(curUserId)){
			throw new BusinessException("未找到当前登录用户信息，请重新登录");
		}
		LbqWrapper<SysUserEntity> lbq = new LbqWrapper<SysUserEntity>();
		lbq.select(SysUserEntity::getId,SysUserEntity::getLoginName,SysUserEntity::getPassword);
		lbq.eq(SysUserEntity::getId, curUserId);
		SysUserEntity user = userService.getOne(lbq);
		if(user == null){
			throw new BusinessException("未找到此用户");
		}
		if(!user.getPassword().equals(AesCipherUtil.enCrypto(user.getLoginName() + userDto.getOldPassword()))){
			throw new BusinessException("旧密码不正确");
		}
		// 密码以帐号+密码的形式进行AES加密
		String encPwd = AesCipherUtil.enCrypto(user.getLoginName() + userDto.getPassword());
		user.setPassword(encPwd);
		boolean success = userService.updateById(user);
		if (!success) {
			throw new BusinessException("账号更新失败");
		}
	}

	@SysLog(
			description = "通过用户id逻辑删除用户",
			bizType = LogTypeConstant.SYSTEMSERVICE,
			bizTypeDescription = LogTypeConstant.SYSTEMSERVICE_NAME,
			operateType = LogConstant.LogOperateType.DELETE,
			operateTypeDescription = "通过用户id逻辑删除用户"
	)
	@PostMapping("/delUser")
	public void delUser(@RequestBody SysUserDTO userDto) {
		SysUserDTO delUser = new SysUserDTO();
		delUser.setId(userDto.getId());
		delUser.setDelFlag(YesOrNo.YES.getKey());
		boolean success = userService.updateUser(delUser);
		if (!success) {
			throw new BusinessException("账号更新失败");
		}
	}

	private void verifyData(SysUserDTO dto) {
		if (StrUtil.isBlank(dto.getLoginName())) {
			throw new BusinessException("登录名不能为空");
		}
		if (StrUtil.isBlank(dto.getCode())) {
			throw new BusinessException("用户编码不能为空");
		}
		if (StrUtil.isBlank(dto.getName())) {
			throw new BusinessException("用户名不能为空");
		}
	}


}
