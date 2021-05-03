package com.ykuee.datamaintenance.model.system.menu.dto;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.BeanMapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ykuee.datamaintenance.common.base.model.dto.BaseDTO;
import com.ykuee.datamaintenance.common.base.node.INode;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单信息表
 * </p>
 *
 * @author heguangyue
 * @since 2020-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenuDTO extends BaseDTO<String> implements INode {

	private String searchCode;
	
    private String title;
    
    private String name;

    private String path;

    private String component;

    private String code;

    private Integer sort;

    private String icon;

    private Integer levelType;

    private String parentId;
    
    private String selected;
    
    private String disableFlag;
    
    private String redirect;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<INode> children;

    @Override
    public List<INode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }
}
