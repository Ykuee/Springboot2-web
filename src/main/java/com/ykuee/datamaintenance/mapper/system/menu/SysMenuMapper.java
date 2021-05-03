package com.ykuee.datamaintenance.mapper.system.menu;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ykuee.datamaintenance.model.system.menu.dto.SysMenuDTO;
import com.ykuee.datamaintenance.model.system.menu.entity.SysMenuEntity;


/**
 * @Description: 菜单相关内容
 * @Author: heguangyue
 * @Date: 2020/7/15
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {
    /**
     * 根据角色ID查询菜单
     *
     * @param roleId
     * @return java.util.List<com.ykuee.volkswagen.bean.vo.MenuVO>
     * @author heguangyue
     * @date 2020/8/8 16:35
     */
    List<SysMenuDTO> listSelectedByRoleId(List<String> roleId);
    
    List<SysMenuDTO> listByRoleId(String roleId);


    IPage<SysMenuDTO> selectParentMenu(IPage<SysMenuDTO> menuVOIPage, String searchKey);


	List<SysMenuDTO> getMenuListByUserId(String userId);
}
