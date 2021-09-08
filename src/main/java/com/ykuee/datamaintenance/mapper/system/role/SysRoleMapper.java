package com.ykuee.datamaintenance.mapper.system.role;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykuee.datamaintenance.model.system.role.dto.SysRoleDTO;
import com.ykuee.datamaintenance.model.system.role.entity.SysRoleEntity;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author heguangyue
 * @since 2020-08-08
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

	List<SysRoleDTO> getRoleListByUserId(String userId);

	List<SysRoleDTO> getRolesByUserId(String userId);

    List<SysRoleDTO> selectBySearchCode(SysRoleDTO sysRoleDTO);
}
