package com.venson.changliulab.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.venson.changliulab.entity.dto.SubjectDTO;
import com.venson.changliulab.entity.pojo.EduSubject;
import com.venson.changliulab.entity.excel.SubjectCategory;
import com.venson.changliulab.entity.subject.SubjectTreeNode;
import com.venson.changliulab.listener.SubjectExcelListener;
import com.venson.changliulab.mapper.EduSubjectMapper;
import com.venson.changliulab.service.admin.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.utils.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-05-10
 */
@Service
@Slf4j
public class EduSubjectServiceImp extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    @CacheEvict(value = "subjectTree",allEntries = true)
    public void saveSubject(MultipartFile multipartFile, EduSubjectService eduSubjectService) {
        try {
            EasyExcel.read(multipartFile.getInputStream(),
                            SubjectCategory.class,
                            new SubjectExcelListener(eduSubjectService))
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    @Cacheable(value = "subjectTree")
    public List<SubjectTreeNode> getAllSubject() {
        LambdaQueryWrapper<EduSubject> wrapper= new LambdaQueryWrapper<>();
        wrapper.select(EduSubject::getId,EduSubject::getTitle,EduSubject::getParentId);
        List<EduSubject> subjectList = baseMapper.selectList(wrapper);

        List<SubjectTreeNode> list = subjectList.stream().filter(o -> o.getParentId() == 0L).map(o ->
            new SubjectTreeNode(o.getId(), o.getTitle(),1)
        ).collect(Collectors.toList());


        Map<Long, List<SubjectTreeNode>> parentIdMap = new HashMap<>();

        subjectList.stream().filter(o->o.getParentId()!=0L).forEach(o->{
            Long parentId = o.getParentId();
            if(parentIdMap.containsKey(parentId)){
                parentIdMap.get(parentId).add(new SubjectTreeNode(o.getId(),o.getTitle(),2));
            }else{
                List<SubjectTreeNode> children = new ArrayList<>();
                children.add(new SubjectTreeNode(o.getId(),o.getTitle(),2));
                parentIdMap.put(parentId,children);
            }
        });

        // the root of subject tree
        list.forEach(o->o.setChildren(parentIdMap.get(o.getId())));
        return list;
    }

    @Override
    @CacheEvict(value = "subjectTree",allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void editSubjectListByTreeNodes(List<SubjectTreeNode> treeNodes) {
        List<EduSubject> addList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();
        List<EduSubject> updateList = new ArrayList<>();

        for (SubjectTreeNode rootNode:
             treeNodes) {
            Long newRootNodeId = processRootNode(rootNode);
            if(rootNode.getChildren().size()!=0){
                rootNode.getChildren().forEach(node->{
                    Long parentId = newRootNodeId!=null? newRootNodeId: rootNode.getId();
                    processNode(node,parentId,addList,updateList,removeList);
                });
            }
        }
        this.removeBatchByIds(removeList);
        this.saveBatch(addList);
        this.updateBatchById(updateList);
    }

    @Override
    public SubjectDTO getSubjectById(Long subjectId) {
        EduSubject subject = baseMapper.selectById(subjectId);
//        Assert.notNull(subject,"Subject Not Found");
        return subject ==null? null: new SubjectDTO(subject.getId(),subject.getTitle());
//        return new SubjectDTO(subject.getId(),subject.getTitle());
    }

    public void processNode(SubjectTreeNode node,Long parentId,List<EduSubject> addList, List<EduSubject> updateList,List<Long> removeList){
        if(node!=null){
            if (node.getAddNew()!=null && node.getAddNew()) {
                EduSubject newTemp = new EduSubject();
                newTemp.setTitle(node.getTitle());
                newTemp.setParentId(parentId);
                addList.add(newTemp);
            } else if (node.getRemove()!=null && node.getRemove()) {
                removeList.add(node.getId());
            } else if (node.getUpdate()!=null && node.getUpdate() && !Objects.equals(node.getId(), parentId)) {
                EduSubject updateTemp = new EduSubject();
                updateTemp.setId(node.getId());
                updateTemp.setTitle(node.getTitle());
                updateTemp.setParentId(parentId);
                updateList.add(updateTemp);
            }
        }
    }
    public Long processRootNode(SubjectTreeNode node){
        if(node!=null){
            if ((node.getAddNew()!=null && node.getAddNew())
                    && (node.getRemove()!=null && !node.getRemove())) {
                EduSubject newTemp = new EduSubject();
                newTemp.setTitle(node.getTitle());
                newTemp.setParentId(0L);
                baseMapper.insert(newTemp);
                return newTemp.getId();
            } else if (node.getRemove()!=null && node.getRemove()) {
                baseMapper.deleteById(node.getId());
            } else if (node.getUpdate()!=null && node.getUpdate()) {
                EduSubject updateTemp = new EduSubject();
                updateTemp.setId(node.getId());
                updateTemp.setTitle(node.getTitle());
                updateTemp.setParentId(0L);
                baseMapper.updateById(updateTemp);
            }
        }
        return null;
    }
}
