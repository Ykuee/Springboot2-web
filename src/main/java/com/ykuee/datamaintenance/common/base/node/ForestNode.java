package com.ykuee.datamaintenance.common.base.node;

/**
 * @Description: //TODO
 * @Author: heguangyue
 * @Date: Created in 2020/7/15
 */
public class ForestNode extends BaseNode {
  private static final long serialVersionUID = 1L;
  private Object content;

  public ForestNode(String id, String parentId, Object content) {
    this.id = id;
    this.parentId = parentId;
    this.content = content;
  }

  public Object getContent() {
    return this.content;
  }

  public void setContent(final Object content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "ForestNode(content=" + this.getContent() + ")";
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof ForestNode)) {
      return false;
    } else {
      ForestNode other = (ForestNode)o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        Object this$content = this.getContent();
        Object other$content = other.getContent();
        if (this$content == null) {
          if (other$content != null) {
            return false;
          }
        } else if (!this$content.equals(other$content)) {
          return false;
        }

        return true;
      }
    }
  }

  @Override
  protected boolean canEqual(final Object other) {
    return other instanceof ForestNode;
  }

  @Override
  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $content = this.getContent();
    result = result * 59 + ($content == null ? 43 : $content.hashCode());
    return result;
  }
}
