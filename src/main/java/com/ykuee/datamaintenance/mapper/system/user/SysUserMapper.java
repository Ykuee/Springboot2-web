package com.ykuee.datamaintenance.mapper.system.user;


import com.ykuee.datamaintenance.model.system.user.dto.SysUserDTO;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykuee.datamaintenance.model.system.user.entity.SysUserEntity;

import java.util.List;

/**
 * @Description:与用户相关内容
 * @Author: Ykuee
 * @Date:15:57 2021-09-08
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUserEntity>{

    List<SysUserDTO> gerUserList(SysUserDTO userDto);

}
