package com.ykuee.datamaintenance.common.base.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: //TODO
 * @Author: heguangyue
 * @Date: Created in 2020/7/15
 */
public class ForestNodeManager<T extends INode> {
  private List<T> list;
  private List<String> parentIds = new ArrayList<String>();

  public ForestNodeManager(List<T> items) {
    this.list = items;
  }

  public INode getTreeNodeAT(String id) {
    Iterator var2 = this.list.iterator();

    INode forestNode;
    do {
      if (!var2.hasNext()) {
        return null;
      }

      forestNode = (INode)var2.next();
    } while(!forestNode.getId().equals(id));

    return forestNode;
  }

  public void addParentId(String parentId) {
    this.parentIds.add(parentId);
  }

  public List<T> getRoot() {
    List<T> roots = new ArrayList();
    Iterator var2 = this.list.iterator();

    while(true) {
      INode forestNode;
      do {
        if (!var2.hasNext()) {
          return roots;
        }

        forestNode = (INode)var2.next();
      } while(!forestNode.getParentId().equals("0") && !this.parentIds.contains(forestNode.getId()));

      roots.add((T) forestNode);
    }
  }
}
