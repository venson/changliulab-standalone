package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import com.venson.changliulabstandalone.entity.pojo.*;
import com.venson.changliulabstandalone.entity.vo.admin.ListQueryParams;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.service.admin.*;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import com.venson.changliulabstandalone.entity.dto.CourseInfoDTO;
import com.venson.changliulabstandalone.entity.dto.CoursePageDTO;
import com.venson.changliulabstandalone.entity.dto.CoursePreviewVo;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.mapper.EduCourseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-05-11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EduCourseServiceImp extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    // base service
    private final EduChapterService chapterService;
    private final EduSectionService sectionService;
    private final EduCourseDescriptionService courseDescriptionService;

    private final EduMemberService memberService;

    private final EduSubjectService subjectService;



    @Override
    @Transactional()
//    @Deprecated
    public Long addCourse(CourseInfoDTO courseInfoDTO) {

        Assert.isTrue(isTitleUsable(null, courseInfoDTO.getTitle()), "Duplicated title");
        EduCourse newCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoDTO,newCourse);
        int insert = baseMapper.insert(newCourse);
        if (insert ==0){
            throw new CustomizedException(20001,"添加课程失败");
        }
        Long id = newCourse.getId();
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoDTO.getDescription());
        courseDescription.setId(id);
        courseDescriptionService.save(courseDescription);
        return id;
    }

    private boolean isTitleUsable(Long id, String title) {
        if(StringUtils.hasText(title)){
            LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(EduCourse::getTitle, title);
            if(id != null){
                wrapper.ne(EduCourse::getId,id);
            }
            EduCourse eduCourse = baseMapper.selectOne(wrapper);
            return eduCourse==null;
        }
        return false;
    }

    @Override
    public CourseInfoDTO getCourseById(Long id) {
        EduCourse eduCourse = baseMapper.selectById(id);
        EduCourseDescription courseDescription = courseDescriptionService.getById(id);
        Assert.notNull(eduCourse, "The Course does not exist");
        Assert.notNull(courseDescription, "");
        CourseInfoDTO infoVo = new CourseInfoDTO();
        BeanUtils.copyProperties(eduCourse,infoVo);
        BeanUtils.copyProperties(courseDescription,infoVo);
        return infoVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCourse(CourseInfoDTO infoVo) {
        Long id = infoVo.getId();
        String title = infoVo.getTitle();
        String description = infoVo.getDescription();
        EduCourse course = baseMapper.selectById(infoVo.getId());
        Assert.isTrue(isTitleUsable(id, title), "Duplicated Course title");
        Assert.notNull(course, "Course not exists");
        BeanUtils.copyProperties(infoVo, course);
        course.setIsModified(true);
        baseMapper.updateById(course);
        EduCourseDescription eduDescription = courseDescriptionService.getById(infoVo.getId());
        if(StringUtils.hasText(description) && description.equals(eduDescription.getDescription()))
            eduDescription.setDescription(description);
        courseDescriptionService.updateById(eduDescription);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setRemoveMarkByCourseById(Long courseId) {

        // mark isRemoveAfterReview, deletion will perform after review.
        chapterService.deleteChapterSectionByCourseId(courseId);

        EduCourse eduCourse = baseMapper.selectById(courseId);
        Assert.notNull(eduCourse, "The Course does not exist");
        eduCourse.setIsRemoveAfterReview(true);
        baseMapper.updateById(eduCourse);
    }



    @Override
    public PageResponse<EduCourse> getPageCoursePublishVo(Integer pageNum, Integer limit, String condition) {


        Page<EduCourse> page = new Page<>(pageNum, limit);
        // 不能使用LambdaQueryWrapper
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(condition),EduCourse::getTitle,condition);
        baseMapper.selectPage(page,wrapper);
        return PageUtil.toBean(page);

    }

    @Override
    public CoursePreviewVo getCoursePreviewById(Long courseId) {
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CoursePreviewVo previewVo = new CoursePreviewVo();
        BeanUtils.copyProperties(eduCourse,previewVo);
        // set course desc
        EduCourseDescription courseDesc = courseDescriptionService.getById(courseId);
        previewVo.setDescription(courseDesc.getDescription());
        EduMember member = memberService.getById(eduCourse.getMemberId());

        previewVo.setAvatar(member.getAvatar());
        previewVo.setMemberName(member.getName());
        previewVo.setMemberTitle(member.getTitle());

        EduSubject subject = subjectService.getById(eduCourse.getSubjectId());
        EduSubject parentSubject = subjectService.getById(subject.getParentId());
        previewVo.setL1Subject(parentSubject.getTitle());
        previewVo.setL2Subject(subject.getTitle());



        return previewVo;
    }

    @Override
    public PageResponse<CoursePageDTO> getCoursePageReview(Integer current, Integer size) {
        Map<Long, ReviewStatus> chapterReviewStatusMap = chapterService.getChapterReviewStatusMap(true);
        Map<Long, ReviewStatus> sectionReviewStatusMap = sectionService.getSectionReviewStatusMap(true);
        LambdaUpdateWrapper<EduCourse> courseWrapper = Wrappers.lambdaUpdate(EduCourse.class);

        Set<Long> courseIdSet = chapterReviewStatusMap
                .entrySet().stream().filter(o->o.getValue()==ReviewStatus.APPLIED)
                .map(Map.Entry::getKey).collect(Collectors.toSet());
        Page<EduCourse> page = new Page<>(current,size);
        Page<CoursePageDTO> dtoPage = new Page<>(current,size);

        Set<Long> sectionCourseIdSet= sectionReviewStatusMap
                .entrySet().stream().filter(o->o.getValue()==ReviewStatus.APPLIED)
                .map(Map.Entry::getKey).collect(Collectors.toSet());
        // create applied course id set
        courseIdSet.addAll(sectionCourseIdSet);
        courseWrapper.eq(EduCourse::getReview, ReviewStatus.APPLIED);
        if(courseIdSet.size()>0){
            courseWrapper.or().in(EduCourse::getId,courseIdSet);
        }
        baseMapper.selectPage(page,courseWrapper);
        LinkedList<CoursePageDTO> dtoRecords = new LinkedList<>();
        dtoPage.setRecords(dtoRecords);
        page.getRecords().forEach(course ->{
            CoursePageDTO temp = new CoursePageDTO();
            BeanUtils.copyProperties(course,temp);
            temp.setInfoReview(course.getReview());
            temp.setChapterReview(chapterReviewStatusMap.get(course.getId()));
            temp.setSectionReview(sectionReviewStatusMap.get(course.getId()));
            dtoRecords.add(temp);
        });

        return PageUtil.toBean(dtoPage);
    }

    @Override
    public PageResponse<CoursePageDTO> getCoursePage(Integer current, Integer size, String condition) {
        Map<Long, ReviewStatus> chapterReviewStatusMap = chapterService.getChapterReviewStatusMap(false);
        Map<Long, ReviewStatus> sectionReviewStatusMap = sectionService.getSectionReviewStatusMap(false);
        Page<EduCourse> page = new Page<>(current,size);
        Page<CoursePageDTO> dtoPage = new Page<>(current,size);

        LambdaQueryWrapper<EduCourse> wrapper = Wrappers.lambdaQuery();
        if(StringUtils.hasText(condition)){
            wrapper.like(EduCourse::getTitle,condition);
        }
        // create applied course id set
        baseMapper.selectPage(page,wrapper);
        LinkedList<CoursePageDTO> dtoRecords = new LinkedList<>();
        dtoPage.setRecords(dtoRecords);
        page.getRecords().forEach(course ->{
            CoursePageDTO temp = new CoursePageDTO();
            BeanUtils.copyProperties(course,temp);
            temp.setInfoReview(course.getReview());
            temp.setChapterReview(chapterReviewStatusMap.get(course.getId()));
            temp.setChapterReview(sectionReviewStatusMap.get(course.getId()));
            dtoRecords.add(temp);
        });
        return PageUtil.toBean(dtoPage);
    }

    @Override
    public PageResponse<EduCourse> getPageCoursePublishVo(ListQueryParams params) {

        Page<EduCourse> page = new Page<>(params.page(),params.perPage());
        // 不能使用LambdaQueryWrapper
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduCourse::getId, EduCourse::getTitle, EduCourse::getReview,
                EduCourse::getIsPublished, EduCourse::getIsModified, EduCourse::getIsRemoveAfterReview);
        baseMapper.selectPage(page,wrapper);
        return PageUtil.toBean(page);
    }

    @Override
    public List<ReviewBasicDTO> getInfoByReviews(Map<ReviewType, List<EduReview>> courseRelates) {
        if(courseRelates.isEmpty()){
            return Collections.emptyList();
        }
        List<Long> sectionIds = courseRelates.getOrDefault(ReviewType.SECTION, Collections.emptyList()).stream()
                .map(EduReview::getRefId).collect(Collectors.toList());
        List<Long> chapterIds = courseRelates.getOrDefault(ReviewType.CHAPTER, Collections.emptyList())
                .stream()
                .map(EduReview::getRefId)
                .collect(Collectors.toList());
        List<Long> courseIds = courseRelates.getOrDefault(ReviewType.COURSE, Collections.emptyList())
                .stream()
                .map(EduReview::getRefId)
                .collect(Collectors.toList());
        Map<Long, EduSection> sectionMap = new HashMap<>();
        Map<Long, EduChapter> chapterMap = new HashMap<>();

        if(!sectionIds.isEmpty()){
            List<EduSection> eduSections = sectionService.listByIds(sectionIds);
            sectionMap.putAll(eduSections.stream().collect(Collectors.toMap(EduSection::getId, Function.identity())));
            chapterIds.addAll(eduSections.stream().map(EduSection::getChapterId).toList());
        }

        if(!chapterIds.isEmpty()){
            List<EduChapter> chapterList = chapterService.listByIds(chapterIds);
            courseIds.addAll(chapterList.stream().map(EduChapter::getCourseId).toList());
            chapterMap.putAll(chapterList.stream().collect(Collectors.toMap(EduChapter::getId,Function.identity())));
        }

        List<EduCourse> courseList = baseMapper.selectBatchIds(courseIds);
        Map<Long, EduCourse> courseMap = new HashMap<>(courseList.stream().collect(Collectors.toMap(EduCourse::getId, Function.identity())));
        return  courseRelates.values().stream().flatMap(Collection::stream).map(review ->ReviewBasicDTO.builder()
                        .id(review.getId())
                        .review(review.getStatus())
                        .type(review.getRefType())
                        .id(review.getId())
                        .refId(review.getRefId())
                        .gmtCreate(review.getGmtCreate())
                        .title(getTitleByIdType(review.getRefId(),review.getRefType(),sectionMap,chapterMap,courseMap))
                        .subTitle(getSubTitleByIdType(review.getRefId(),review.getRefType(),sectionMap,chapterMap,courseMap))
                .build())
                .toList();
    }

    private String getTitleByIdType(Long refId, ReviewType refType, Map<Long, EduSection> sectionMap, Map<Long, EduChapter> chapterMap, Map<Long, EduCourse> courseMap) {
        return switch (refType) {
            case SECTION -> sectionMap.get(refId).getTitle();
            case CHAPTER -> chapterMap.get(refId).getTitle();
            case COURSE -> courseMap.get(refId).getTitle();
            default -> null;
        };
    }
    private String getSubTitleByIdType(Long refId, ReviewType refType, Map<Long, EduSection> sectionMap, Map<Long, EduChapter> chapterMap, Map<Long, EduCourse> courseMap) {
        if(refType.equals(ReviewType.SECTION)){
            Long courseId = sectionMap.get(refId).getCourseId();
            Long chapterId= sectionMap.get(refId).getChapterId();

            return courseMap.get(courseId).getTitle() + "-" + chapterMap.get(chapterId).getTitle();
        }
        if(refType.equals(ReviewType.CHAPTER)){
            Long courseId = chapterMap.get(refId).getCourseId();
            return courseMap.get(courseId).getTitle() ;
        }
        return null;
    }


}
