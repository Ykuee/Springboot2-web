/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.ykuee.datamaintenance.common.base.approval;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykuee.datamaintenance.model.approvalRecard.ApprovalRecardEntity;
import com.ykuee.datamaintenance.mapper.approvalrecard.ApprovalRecardMapper;
import com.ykuee.datamaintenance.common.base.beancopy.DtoEntityConverter;
import com.ykuee.datamaintenance.common.base.constant.ApproveStatus;
import com.ykuee.datamaintenance.common.base.constant.TransType;
import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.base.serviceImpl.ServiceImpl;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbqWrapper;
import com.ykuee.datamaintenance.common.mybatisplus.sqlwrapper.LbuWrapper;
import com.ykuee.datamaintenance.common.support.UserUtil;
import com.ykuee.datamaintenance.common.uidgenerator.IdGenerator;
import com.ykuee.datamaintenance.constant.DataSourceType;
import com.ykuee.datamaintenance.constant.YesOrNo;
import com.ykuee.datamaintenance.util.TableNameUtil;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: DataMaintenance
 * @description:
 * @author: Ykuee
 * @create: 2021-08-02 15:46
 **/
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public abstract class ApprovalServiceImpl<M extends BaseMapper<E>, D extends ApprovalBaseDTO, E extends ApprovalBaseEntity> extends ServiceImpl<BaseMapper<E> , E> implements ApprovalService<D, E> {


    @Autowired
    protected DtoEntityConverter<D, E> converter;

    @Autowired
    protected IdGenerator<String> idGenerator;

    @Autowired
    private ApprovalRecardMapper approvalRecardMapper;

    @Autowired
    private UserUtil userUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submit(D dto, TransType transType){
        dto.setStatus(ApproveStatus.APPROVING);
        TableNameUtil.setTableName(DataSourceType.DATA_2);
        E e = converter.toEntity(dto);
        this.updateById(e);
        TableNameUtil.clear();
        E entity = this.getById(String.valueOf(dto.getId()));
        LbqWrapper<ApprovalRecardEntity> aLbq = new LbqWrapper();
        aLbq.select(ApprovalRecardEntity::getId);
        aLbq.eq(ApprovalRecardEntity::getRecardId,dto.getId());
        aLbq.eq(ApprovalRecardEntity::getStatus,ApproveStatus.APPROVING);
        aLbq.eq(ApprovalRecardEntity::getDelFlag, YesOrNo.NO);
        aLbq.eq(ApprovalRecardEntity::getTransType, transType);
        ApprovalRecardEntity updateEntity = approvalRecardMapper.selectOne(aLbq);
        ApprovalRecardEntity approvalEntity = getApprovalRecardEntity(dto, transType, entity);
        if(updateEntity != null){
            LbuWrapper<ApprovalRecardEntity> aLbu = new LbuWrapper();
            aLbu.set(ApprovalRecardEntity::getDelFlag, YesOrNo.YES);
            aLbu.eq(ApprovalRecardEntity::getRecardId,dto.getId());
            aLbu.eq(ApprovalRecardEntity::getStatus,ApproveStatus.APPROVING);
            aLbu.eq(ApprovalRecardEntity::getId,updateEntity.getId());
            aLbu.eq(ApprovalRecardEntity::getTransType, transType);
            approvalRecardMapper.update(null,aLbu);
        }
        approvalRecardMapper.insert(approvalEntity);
        doAfterSumbit(dto,entity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean agree(D dto, TransType transType) throws Exception {
        dto.setStatus(ApproveStatus.APPROVED);
        TableNameUtil.setTableName(DataSourceType.DATA_2);
        E e = converter.toEntity(dto);
        this.updateById(e);
        TableNameUtil.clear();
        LbqWrapper<ApprovalRecardEntity> aLbq = new LbqWrapper();
        aLbq.select(ApprovalRecardEntity::getId,
                ApprovalRecardEntity::getEntityData,
                ApprovalRecardEntity::getEntityClass);
        aLbq.eq(ApprovalRecardEntity::getRecardId,dto.getId());
        aLbq.eq(ApprovalRecardEntity::getStatus,ApproveStatus.APPROVING);
        aLbq.eq(ApprovalRecardEntity::getTransType, transType);
        aLbq.eq(ApprovalRecardEntity::getDelFlag, YesOrNo.NO);
        ApprovalRecardEntity updateEntity = approvalRecardMapper.selectOne(aLbq);
        if(updateEntity == null){
            throw new BusinessException("未找到审批记录");
        }
        LbuWrapper<ApprovalRecardEntity> aLbu = new LbuWrapper();
        aLbu.eq(ApprovalRecardEntity::getRecardId,dto.getId());
        aLbu.eq(ApprovalRecardEntity::getStatus,ApproveStatus.APPROVING);
        aLbu.eq(ApprovalRecardEntity::getId,updateEntity.getId());
        aLbu.eq(ApprovalRecardEntity::getDelFlag,YesOrNo.NO);
        aLbu.eq(ApprovalRecardEntity::getTransType, transType);
        aLbu.set(ApprovalRecardEntity::getStatus, ApproveStatus.APPROVED);
        aLbu.set(ApprovalRecardEntity::getApproveBy, userUtil.getUserId());
        aLbu.set(ApprovalRecardEntity::getApproveDate, new Date());
        approvalRecardMapper.update(null,aLbu);
        String jsonData = updateEntity.getEntityData();
        E pubEntity = JSONObject.parseObject(jsonData,ClassUtil.loadClass(updateEntity.getEntityClass()));
        TableNameUtil.setTableName(DataSourceType.DATA_3);
        QueryWrapper<E> qw = new QueryWrapper<E>();
        qw.eq("id",dto.getId());
        qw.eq("del_flag",YesOrNo.NO.getKey());
        E dataEntity = this.getOne(qw);
        if(dataEntity != null){
            UpdateWrapper<E> uw = new UpdateWrapper<E>();
            uw.eq("id",dto.getId());
            uw.eq("del_flag",YesOrNo.NO.getKey());
            this.update(pubEntity,uw);
        }else{
            this.save(pubEntity);
        }
        TableNameUtil.clear();
        doAfterAgree(dto,pubEntity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reject(D dto, TransType transType) throws Exception {
        dto.setStatus(ApproveStatus.REJECT);
        TableNameUtil.setTableName(DataSourceType.DATA_2);
        E e = converter.toEntity(dto);
        this.updateById(e);
        TableNameUtil.clear();
        LbqWrapper<ApprovalRecardEntity> aLbq = new LbqWrapper();
        aLbq.select(ApprovalRecardEntity::getId);
        aLbq.eq(ApprovalRecardEntity::getRecardId,dto.getId());
        aLbq.eq(ApprovalRecardEntity::getStatus,ApproveStatus.APPROVING);
        aLbq.eq(ApprovalRecardEntity::getTransType, transType);
        ApprovalRecardEntity updateEntity = approvalRecardMapper.selectOne(aLbq);
        if(updateEntity == null){
            throw new BusinessException("未找到审批记录");
        }
        LbuWrapper<ApprovalRecardEntity> aLbu = new LbuWrapper();
        aLbu.eq(ApprovalRecardEntity::getRecardId,dto.getId());
        aLbu.eq(ApprovalRecardEntity::getStatus,ApproveStatus.APPROVING);
        aLbu.eq(ApprovalRecardEntity::getId,updateEntity.getId());
        aLbu.eq(ApprovalRecardEntity::getDelFlag,YesOrNo.NO);
        aLbu.eq(ApprovalRecardEntity::getTransType, transType);
        aLbu.set(ApprovalRecardEntity::getStatus, ApproveStatus.REJECT);
        aLbu.set(ApprovalRecardEntity::getApproveBy, userUtil.getUserId());
        aLbu.set(ApprovalRecardEntity::getApproveDate, new Date());
        approvalRecardMapper.update(null,aLbu);
        return true;
    }

    private ApprovalRecardEntity getApprovalRecardEntity(D dto, TransType transType, E entity) {
        ApprovalRecardEntity approvalEntity = new ApprovalRecardEntity();
        approvalEntity.setId(idGenerator.generate());
        approvalEntity.setRecardId(String.valueOf(dto.getId()));
        approvalEntity.setStatus(ApproveStatus.APPROVING);
        approvalEntity.setEntityData(JsonUtil.toJson(entity));
        approvalEntity.setDtoData(JsonUtil.toJson(dto));
        approvalEntity.setTransType(transType);
        approvalEntity.setDelFlag(YesOrNo.NO.getKey());
        approvalEntity.setDtoClass(dto.getClass().getCanonicalName());
        approvalEntity.setEntityClass(entity.getClass().getCanonicalName());
        approvalEntity.setSubmitDate(new Date());
        approvalEntity.setSubmitBy(userUtil.getUserId());
        return approvalEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<D> getApprovingData(TransType transType) throws Exception {
        LbqWrapper<ApprovalRecardEntity> lbq = new LbqWrapper();
        lbq.eq(ApprovalRecardEntity::getStatus,ApproveStatus.APPROVING);
        lbq.eq(ApprovalRecardEntity::getTransType, transType);
        lbq.eq(ApprovalRecardEntity::getDelFlag, YesOrNo.NO);
        List<ApprovalRecardEntity> list = approvalRecardMapper.selectList(lbq);
        if(list == null && list.size() == 0){
            throw null;
        }
        List<D> resList = new ArrayList<>();
        for(ApprovalRecardEntity entity : list){
            String jsonData = entity.getDtoData();
            D dto = JSONObject.parseObject(jsonData,ClassUtil.loadClass(entity.getDtoClass()));
            resList.add(dto);
        }
        return resList;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> getApprovingJsonData(TransType transType) throws BusinessException {
        LbqWrapper<ApprovalRecardEntity> lbq = new LbqWrapper();
        lbq.select(ApprovalRecardEntity::getId);
        lbq.eq(ApprovalRecardEntity::getStatus,ApproveStatus.APPROVING);
        lbq.eq(ApprovalRecardEntity::getTransType, transType);
        List<ApprovalRecardEntity> list = approvalRecardMapper.selectList(lbq);
        if(list == null && list.size() == 0){
            throw null;
        }
        List<String> resList = new ArrayList<>();
        for(ApprovalRecardEntity entity : list){
            resList.add(entity.getDtoData()) ;
        }
        return resList;
    }

    @Transactional(rollbackFor = Exception.class)
    public abstract void doAfterSumbit(D dto,E entity);

    @Transactional(rollbackFor = Exception.class)
    public abstract void doAfterAgree(D dto, E entity);

}
