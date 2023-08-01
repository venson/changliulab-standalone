package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.SubjectDTO;
import com.venson.changliulab.entity.pojo.EduSubject;
import com.venson.changliulab.entity.subject.SubjectTreeNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-05-10
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile multipartFile, EduSubjectService eduSubjectService);

    List<SubjectTreeNode> getAllSubject();

    void editSubjectListByTreeNodes(List<SubjectTreeNode> treeNodes);

    SubjectDTO getSubjectById(Long subjectId);
}
