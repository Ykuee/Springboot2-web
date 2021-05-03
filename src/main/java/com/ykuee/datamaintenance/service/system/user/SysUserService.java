package com.ykuee.datamaintenance.service.system.user;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykuee.datamaintenance.model.system.user.dto.SysUserDTO;
import com.ykuee.datamaintenance.model.system.user.entity.SysUserEntity;

public interface SysUserService extends IService<SysUserEntity>{
	
	public boolean updateUser(SysUserDTO userInfo);

	SysUserDTO getUser(SysUserDTO userDto);

	public List<SysUserDTO> gerUserList(SysUserDTO userDto);

}
