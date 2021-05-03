package com.ykuee.datamaintenance.common.base.node;

import java.util.List;

/**
 * @Description: //TODO
 * @Author: heguangyue
 * @Date: Created in 2020/7/15
 */
public class ForestNodeMerger {
  public ForestNodeMerger() {
  }

  public static <T extends INode> List<T> merge(List<T> items) {
    ForestNodeManager<T> forestNodeManager = new ForestNodeManager(items);
    items.forEach((forestNode) -> {
      if (!forestNode.getParentId().equals("0")) {
        INode node = forestNodeManager.getTreeNodeAT(forestNode.getParentId());
        if (node != null) {
          node.getChildren().add(forestNode);
        } else {
          forestNodeManager.addParentId(forestNode.getId());
        }
      }

    });
    return forestNodeManager.getRoot();
  }
}

