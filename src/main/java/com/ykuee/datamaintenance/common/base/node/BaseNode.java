package com.ykuee.datamaintenance.common.base.node;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: //TODO
 * @Author: heguangyue
 * @Date: Created in 2020/7/15
 */
public class BaseNode implements INode {
  private static final long serialVersionUID = 1L;

  @JsonSerialize(using = ToStringSerializer.class)
  protected String id;

  @JsonSerialize(using = ToStringSerializer.class)
  protected String parentId;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  protected List<INode> children = new ArrayList();


  public BaseNode() {
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public String getParentId() {
    return this.parentId;
  }

  @Override
  public List<INode> getChildren() {
    return this.children;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public void setParentId(final String parentId) {
    this.parentId = parentId;
  }

  public void setChildren(final List<INode> children) {
    this.children = children;
  }


  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof BaseNode)) {
      return false;
    } else {
      BaseNode other = (BaseNode) o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label59:
        {
          Object this$id = this.getId();
          Object other$id = other.getId();
          if (this$id == null) {
            if (other$id == null) {
              break label59;
            }
          } else if (this$id.equals(other$id)) {
            break label59;
          }

          return false;
        }

        Object this$parentId = this.getParentId();
        Object other$parentId = other.getParentId();
        if (this$parentId == null) {
          if (other$parentId != null) {
            return false;
          }
        } else if (!this$parentId.equals(other$parentId)) {
          return false;
        }

        Object this$children = this.getChildren();
        Object other$children = other.getChildren();
        if (this$children == null) {
          if (other$children != null) {
            return false;
          }
        } else if (!this$children.equals(other$children)) {
          return false;
        }
        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof BaseNode;
  }

  @Override
  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $id = this.getId();
    result = result * 59 + ($id == null ? 43 : $id.hashCode());
    Object $parentId = this.getParentId();
    result = result * 59 + ($parentId == null ? 43 : $parentId.hashCode());
    Object $children = this.getChildren();
    result = result * 59 + ($children == null ? 43 : $children.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "BaseNode(id=" + this.getId() + ", parentId=" + this.getParentId() + ", children=" + this.getChildren() + ")";
  }

}