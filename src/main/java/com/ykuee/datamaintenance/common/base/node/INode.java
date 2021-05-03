package com.ykuee.datamaintenance.common.base.node;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: //TODO
 * @Author: heguangyue
 * @Date: Created in 2020/7/15
 */
public interface INode extends Serializable {

  String getId();

  String getParentId();

  List<INode> getChildren();
}
