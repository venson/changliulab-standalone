package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.venson.changliulabstandalone.entity.pojo.EduChapter;
import com.venson.changliulabstandalone.entity.pojo.EduChapterDescription;
import com.venson.changliulabstandalone.entity.pojo.EduSection;
import com.venson.changliulabstandalone.entity.dto.CourseSyllabusDTO;
import com.venson.changliulabstandalone.service.admin.EduChapterDescriptionService;
import com.venson.changliulabstandalone.service.admin.EduChapterService;
import com.venson.changliulabstandalone.service.admin.EduContentService;
import com.venson.changliulabstandalone.service.admin.EduSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EduContentServiceImpl implements EduContentService {
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduChapterDescriptionService chapterDescService;

    @Autowired
    private EduSectionService sectionService;
    @Override
    public List<CourseSyllabusDTO> getSyllabusByCourseId(Long courseId) {
        LambdaQueryWrapper<EduChapter> chapterWrapper = new LambdaQueryWrapper<>();
        chapterWrapper.eq(EduChapter::getCourseId, courseId).orderByAsc(EduChapter::getSort).orderByAsc(EduChapter::getId);
        List<EduChapter> chapters =chapterService.list(chapterWrapper);
        List<Long> chapterIds = chapters.stream().map(EduChapter::getId).toList();
        List<EduChapterDescription> chapterDescriptions =chapterDescService.listByIds(chapterIds);
        // <chapterId, EduChapterDescription> map
        Map<Long, EduChapterDescription> descMap = chapterDescriptions.stream().collect(Collectors.toMap(EduChapterDescription::getId, Function.identity()));
        List<CourseSyllabusDTO> syllabus = chapters.stream().map(o -> new CourseSyllabusDTO(o.getId(),
                o.getTitle(), descMap.get(o.getId()).getDescription(),
                o.getReview(),o.getIsRemoveAfterReview()))
                .collect(Collectors.toList());

        LambdaQueryWrapper<EduSection> sectionWrapper = new LambdaQueryWrapper<>();
        sectionWrapper.eq(EduSection::getCourseId, courseId).orderByAsc(EduSection::getChapterId).orderByAsc(EduSection::getSort).orderByAsc(EduSection::getId);
        List<EduSection> sections = sectionService.list(sectionWrapper);

        HashMap<Long, ArrayList<CourseSyllabusDTO>> chapterIdSectionMap = new LinkedHashMap<>();
        sections.forEach(o->{
            CourseSyllabusDTO section =  new CourseSyllabusDTO(o.getId(),o.getTitle(),null,o.getReview(), o.getIsRemoveAfterReview());
            Long chapterId = o.getChapterId();
            if(chapterIdSectionMap.containsKey(chapterId)){

                chapterIdSectionMap.get(chapterId).add(section);
            }else{
                ArrayList<CourseSyllabusDTO> sectionList = new ArrayList<>();
                sectionList.add(section);
                chapterIdSectionMap.put(chapterId,sectionList);
            }
        } );

        syllabus.forEach(o-> o.setChildren(chapterIdSectionMap.get(o.getId())));
        return syllabus;
    }
}
