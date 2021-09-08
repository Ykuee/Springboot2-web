/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.ykuee.datamaintenance.common.base.approval;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykuee.datamaintenance.common.base.constant.TransType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApprovalService<D, E> extends IService<E> {

    boolean submit(D dto, TransType transType);

    @Transactional(rollbackFor = Exception.class)
    boolean agree(D dto, TransType transType) throws Exception;

    @Transactional(rollbackFor = Exception.class)
    boolean reject(D dto, TransType transType) throws Exception;

    @Transactional(rollbackFor = Exception.class)
    List<D> getApprovingData(TransType transType) throws Exception;

    @Transactional(rollbackFor = Exception.class)
    List<String> getApprovingJsonData(TransType transType) throws Exception;
}
