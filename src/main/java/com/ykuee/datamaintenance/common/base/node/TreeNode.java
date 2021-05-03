package com.ykuee.datamaintenance.common.base.node;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @Description: //TODO
 * @Author: heguangyue
 * @Date: Created in 2020/7/15
 */
public class TreeNode extends BaseNode {
  private static final long serialVersionUID = 1L;
  private String title;
  @JsonSerialize(
          using = ToStringSerializer.class
  )
  private Long key;
  @JsonSerialize(
          using = ToStringSerializer.class
  )
  private Long value;

  public TreeNode() {
  }

  public String getTitle() {
    return this.title;
  }

  public Long getKey() {
    return this.key;
  }

  public Long getValue() {
    return this.value;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public void setKey(final Long key) {
    this.key = key;
  }

  public void setValue(final Long value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "TreeNode(title=" + this.getTitle() + ", key=" + this.getKey() + ", value=" + this.getValue() + ")";
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof TreeNode)) {
      return false;
    } else {
      TreeNode other = (TreeNode)o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label47: {
          Object this$title = this.getTitle();
          Object other$title = other.getTitle();
          if (this$title == null) {
            if (other$title == null) {
              break label47;
            }
          } else if (this$title.equals(other$title)) {
            break label47;
          }

          return false;
        }

        Object this$key = this.getKey();
        Object other$key = other.getKey();
        if (this$key == null) {
          if (other$key != null) {
            return false;
          }
        } else if (!this$key.equals(other$key)) {
          return false;
        }

        Object this$value = this.getValue();
        Object other$value = other.getValue();
        if (this$value == null) {
          if (other$value != null) {
            return false;
          }
        } else if (!this$value.equals(other$value)) {
          return false;
        }

        return true;
      }
    }
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof TreeNode;
  }

  @Override
  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $title = this.getTitle();
    result = result * 59 + ($title == null ? 43 : $title.hashCode());
    Object $key = this.getKey();
    result = result * 59 + ($key == null ? 43 : $key.hashCode());
    Object $value = this.getValue();
    result = result * 59 + ($value == null ? 43 : $value.hashCode());
    return result;
  }
}