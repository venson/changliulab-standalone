package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.dto.ShowSyllabusDTO;
import com.venson.changliulab.entity.enums.ReviewType;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.*;
import com.venson.changliulab.entity.dto.CourseSyllabusDTO;
import com.venson.changliulab.service.admin.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EduContentServiceImp implements EduContentService, ReviewableService {
    private final EduChapterService chapterService;
    private final EduChapterDescriptionService chapterDescService;
    private final EduSectionService sectionService;
    private final EduChapterPublishedService chapterPublishedService;
    private final EduChapterPublishedDescService chapterPublishedDescService;
    private final EduSectionPublishedService sectionPublishedService;

    @Override
    public List<CourseSyllabusDTO> getSyllabusByCourseId(Long courseId) {
        LambdaQueryWrapper<EduChapter> chapterWrapper = new LambdaQueryWrapper<>();
        chapterWrapper.eq(EduChapter::getCourseId, courseId).orderByAsc(EduChapter::getSort).orderByAsc(EduChapter::getId);
        List<EduChapter> chapters = chapterService.list(chapterWrapper);
        if(chapters.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> chapterIds = chapters.stream().map(EduChapter::getId).toList();
        List<EduChapterDescription> chapterDescriptions = chapterDescService.listByIds(chapterIds);
        // <chapterId, EduChapterDescription> map
        Map<Long, EduChapterDescription> descMap = chapterDescriptions.stream().collect(Collectors.toMap(EduChapterDescription::getId, Function.identity()));
        List<CourseSyllabusDTO> syllabus = chapters.stream().map(o -> CourseSyllabusDTO.builder()
                        .title(o.getTitle())
                        .id(o.getId())
                        .review(o.getReview())
                .type(ReviewType.CHAPTER)
                        .isRemoveAfterReview(o.getIsRemoveAfterReview())
                        .isModified(o.getIsModified())
                        .description(descMap.get(o.getId()).getDescription())
                        .build()).collect(Collectors.toList());

//        new CourseSyllabusDTO(o.getId(),
//                        o.getTitle(), descMap.get(o.getId()).getDescription(),
//                .collect(Collectors.toList());
//                        o.getReview(), o.getIsRemoveAfterReview()))

        LambdaQueryWrapper<EduSection> sectionWrapper = new LambdaQueryWrapper<>();
        sectionWrapper.eq(EduSection::getCourseId, courseId).orderByAsc(EduSection::getChapterId).orderByAsc(EduSection::getSort).orderByAsc(EduSection::getId);
        List<EduSection> sections = sectionService.list(sectionWrapper);

        HashMap<Long, ArrayList<CourseSyllabusDTO>> chapterIdSectionMap = new LinkedHashMap<>();
        if(!sections.isEmpty()){
            sections.forEach(o -> {
//                CourseSyllabusDTO section = new CourseSyllabusDTO(o.getId(), o.getTitle(), null, o.getReview(), o.getIsRemoveAfterReview());
                CourseSyllabusDTO section = CourseSyllabusDTO.builder().id(o.getId())
                        .title(o.getTitle())
                        .review(o.getReview())
                        .type(ReviewType.SECTION)
                        .isRemoveAfterReview(o.getIsRemoveAfterReview())
                        .isModified(o.getIsModified())
                        .build();
                Long chapterId = o.getChapterId();
                if (chapterIdSectionMap.containsKey(chapterId)) {

                    chapterIdSectionMap.get(chapterId).add(section);
                } else {
                    ArrayList<CourseSyllabusDTO> sectionList = new ArrayList<>();
                    sectionList.add(section);
                    chapterIdSectionMap.put(chapterId, sectionList);
                }
            });
        }

        syllabus.forEach(o -> o.setChildren(chapterIdSectionMap.get(o.getId())));
        return syllabus;
    }

    @Override
    public List<CourseSyllabusDTO> getReviewedSyllabusByCourseId(Long courseId) {
        LambdaQueryWrapper<EduChapterPublished> chapterWrapper = new LambdaQueryWrapper<>();
        chapterWrapper.eq(EduChapterPublished::getCourseId, courseId).orderByAsc(EduChapterPublished::getSort).orderByAsc(EduChapterPublished::getId);
        List<EduChapterPublished> chapters = chapterPublishedService.list(chapterWrapper);
        if(chapters.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> chapterIds = chapters.stream().map(EduChapterPublished::getId).toList();
        List<EduChapterPublishedDesc> chapterDescriptions = chapterPublishedDescService.listByIds(chapterIds);
        // <chapterId, EduChapterDescription> map
        Map<Long, EduChapterPublishedDesc> descMap = chapterDescriptions.stream().collect(Collectors.toMap(EduChapterPublishedDesc::getId, Function.identity()));
        List<CourseSyllabusDTO> syllabus = chapters.stream().map(o -> CourseSyllabusDTO.builder()
                        .id(o.getId())
                        .title(o.getTitle())
                        .description(descMap.get(o.getId()).getDescription())
//                        .children(new ArrayList<>())
                        .build())
                .collect(Collectors.toList());

        LambdaQueryWrapper<EduSectionPublished> sectionWrapper = new LambdaQueryWrapper<>();
        sectionWrapper.eq(EduSectionPublished::getCourseId, courseId).orderByAsc(EduSectionPublished::getChapterId)
                .orderByAsc(EduSectionPublished::getSort).orderByAsc(EduSectionPublished::getId);
        List<EduSectionPublished> sections = sectionPublishedService.list(sectionWrapper);

        HashMap<Long, List<CourseSyllabusDTO>> chapterIdSectionMap = new LinkedHashMap<>();
        sections.forEach(o -> {

            CourseSyllabusDTO section = CourseSyllabusDTO.builder().id(o.getId()).title( o.getTitle()).build();
            Long chapterId = o.getChapterId();
            if (chapterIdSectionMap.containsKey(chapterId)) {

                chapterIdSectionMap.get(chapterId).add(section);
            } else {
                List<CourseSyllabusDTO> sectionList = new LinkedList<>();
                sectionList.add(section);
                chapterIdSectionMap.put(chapterId, sectionList);
            }
        });

        syllabus.forEach(o -> o.setChildren(chapterIdSectionMap.get(o.getId())));
        return syllabus;
    }

    @Override
    public ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id) {

        List<CourseSyllabusDTO> syllabusReviewed = getReviewedSyllabusByCourseId(id);
        List<CourseSyllabusDTO> syllabusApplied = getSyllabusByCourseId(id);
        ShowSyllabusDTO applied = ShowSyllabusDTO.builder().id(id)
//                .title()
                .syllabus(syllabusApplied)
                .build();
        ShowSyllabusDTO reviewed= ShowSyllabusDTO.builder().id(id)
//                .title()
                .syllabus(syllabusReviewed)
                .build();
        return ReviewTargetDTO.<ReviewAble>builder()
                .id(id)
                .reviewed(reviewed)
                .applied(applied)
                .build();
    }
}
