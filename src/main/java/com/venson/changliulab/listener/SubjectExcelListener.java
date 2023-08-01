package com.venson.changliulab.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venson.changliulab.entity.pojo.EduSubject;
import com.venson.changliulab.entity.excel.SubjectCategory;
import com.venson.changliulab.exception.CustomizedException;
import com.venson.changliulab.service.admin.EduSubjectService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubjectExcelListener extends AnalysisEventListener<SubjectCategory> {

    public EduSubjectService eduSubjectService;


    public SubjectExcelListener(EduSubjectService eduSubjectService){
        this.eduSubjectService = eduSubjectService;
    }

    @Override
    public void invoke(SubjectCategory subjectCategory, AnalysisContext analysisContext) {
        if(subjectCategory ==null){
            throw new CustomizedException(20001, "数据为空");
        }

        EduSubject eduSubjectTop = this.topSubjectExist(eduSubjectService, subjectCategory.getTopSubject());
        if(eduSubjectTop !=null){
            log.info("=====" + eduSubjectTop);
        }
        if (eduSubjectTop == null){
            eduSubjectTop = new EduSubject();
            eduSubjectTop.setParentId(0L);
            eduSubjectTop.setTitle(subjectCategory.getTopSubject());
            eduSubjectService.save(eduSubjectTop);
        }

        log.info(eduSubjectTop.toString());

        Long parentId = eduSubjectTop.getId();
        EduSubject eduSubjectLevelI = this.levelISubjectExist(eduSubjectService,
                subjectCategory.getLevelISubject(),
                parentId);
        if (eduSubjectLevelI == null){
            eduSubjectLevelI = new EduSubject();
            eduSubjectLevelI.setParentId(parentId);
            eduSubjectLevelI.setTitle(subjectCategory.getLevelISubject());
            eduSubjectService.save(eduSubjectLevelI);

        }


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    private EduSubject topSubjectExist(EduSubjectService eduSubjectService, String name){
        QueryWrapper<EduSubject> wrapper= new QueryWrapper<>();
        wrapper.eq("title" ,name);
        wrapper.eq("parent_id", 0);
        return eduSubjectService.getOne(wrapper);
    }

    private EduSubject levelISubjectExist(EduSubjectService eduSubjectService, String name, Long parent_id){
        QueryWrapper<EduSubject> wrapper= new QueryWrapper<>();
        wrapper.eq("title" ,name);
        wrapper.eq("parent_id", parent_id);
        return eduSubjectService.getOne(wrapper);
    }
}
