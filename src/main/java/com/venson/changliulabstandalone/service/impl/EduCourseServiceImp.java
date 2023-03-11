package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulabstandalone.entity.pojo.EduCourse;
import com.venson.changliulabstandalone.entity.pojo.EduCourseDescription;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduSubject;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
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
public class EduCourseServiceImp extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    // base service
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduSectionService sectionService;
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduMemberService memberService;

    @Autowired
    private EduSubjectService subjectService;



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


}
