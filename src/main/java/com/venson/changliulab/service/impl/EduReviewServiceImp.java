package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.entity.dto.ReviewBasicDTO;
import com.venson.changliulab.entity.dto.ReviewDTO;
import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.pojo.EduReviewMsg;
import com.venson.changliulab.entity.vo.admin.ListQueryParams;
import com.venson.changliulab.entity.vo.admin.ReviewMetaVo;
import com.venson.changliulab.mapper.EduReviewMapper;
import com.venson.changliulab.service.EduReportPublishedService;
import com.venson.changliulab.service.EduReportService;
import com.venson.changliulab.service.admin.*;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-07-16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EduReviewServiceImp extends ServiceImpl<EduReviewMapper, EduReview> implements EduReviewService{


    private final EduReviewMsgService reviewMsgService;
    private final ReviewServiceFactory reviewServiceFactory;

    private final EduActivityService activityService;
    private final EduActivityPublishedService activityPublishedService;
    private final EduCourseService courseService;
    private final EduMethodologyService methodologyService;
    private final EduMethodologyPublishedService methodologyPublishedService;
    private final EduResearchService researchService;
    private final EduResearchPublishedService researchPublishedService;
    private final EduReportService reportService;
    private final EduReportPublishedService reportPublishedService;

    @Override
    public List<EduReview> getReviewByMethodologyId(Long id) {
        LambdaQueryWrapper<EduReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduReview::getRefType, ReviewType.METHODOLOGY)
                .eq(EduReview::getRefId, id)
                .orderByAsc(EduReview::getId);
        return baseMapper.selectList(wrapper);
    }

//    @Override
//    public List<ReviewDTO> getReviewListByRefId(Long id, ReviewType type) {
//        LambdaQueryWrapper<EduReview> reviewWrapper = Wrappers.lambdaQuery(EduReview.class);
//        reviewWrapper.eq(EduReview::getRefId, id)
//                .eq(EduReview::getRefType, type)
//                .orderByDesc(EduReview::getId);
//        List<EduReview> reviewList = baseMapper.selectList(reviewWrapper);
//        LambdaQueryWrapper<EduReviewMsg> reviewMsgWrapper = Wrappers.lambdaQuery(EduReviewMsg.class);
//        reviewMsgWrapper.eq(EduReviewMsg::getRefId, id)
//                .eq(EduReviewMsg::getRefType, type)
//                .orderByDesc(EduReviewMsg::getId);
//        List<EduReviewMsg> msgList = reviewMsgService.list(reviewMsgWrapper);
//        HashMap<Long, List<EduReviewMsg>> msgMap = new HashMap<>();
//        msgList.forEach(msg->{
//            Long reviewId =  msg.getReviewId();
//            if(msgMap.containsKey(reviewId)){
//                List<EduReviewMsg> list = msgMap.get(reviewId);
//                list.add(msg);
//            }else{
//                LinkedList<EduReviewMsg> list = new LinkedList<>();
//                list.add(msg);
//                msgMap.put(reviewId,list);
//            }
//        });
//        LinkedList<ReviewDTO> reviewDTOs = new LinkedList<>();
//        reviewList.forEach(review ->{
//            ReviewDTO temp = new ReviewDTO();
//            BeanUtils.copyProperties(review,temp);
//            temp.setMessages(msgMap.get(temp.getId()));
//            reviewDTOs.add(temp);
//        });
//
//        return reviewDTOs;
//    }

    @Override
    public PageResponse<ReviewBasicDTO> getPageReview(ListQueryParams params) {
        List<HashMap<String, String>> filter = params.getFilter();
        String type = filter.get(0).getOrDefault("type", "");
        LambdaQueryWrapper<EduReview> wrapper = Wrappers.lambdaQuery();
        Page<EduReview> page = new Page<>(params.page(),params.perPage());
        Page<ReviewBasicDTO> pageNew = new Page<>(params.page(),params.perPage());
        switch (type){
            case "activity" -> wrapper.eq(EduReview::getRefType, ReviewType.ACTIVITY);
            case "research" -> wrapper.eq(EduReview::getRefType, ReviewType.RESEARCH);
            case "methodology" -> wrapper.eq(EduReview::getRefType, ReviewType.METHODOLOGY);
            case "course" -> wrapper.in(EduReview::getRefType, ReviewType.COURSE, ReviewType.CHAPTER, ReviewType.SECTION);
            case "report" -> wrapper.eq(EduReview::getRefType, ReviewType.REPORT);
            default -> wrapper.eq(EduReview::getStatus, ReviewStatus.APPLIED);

        }
        wrapper.select(EduReview::getRefId,EduReview::getRefType, EduReview::getId,EduReview::getGmtCreate,EduReview::getStatus);
        baseMapper.selectPage(page,wrapper);
        BeanUtils.copyProperties(page,pageNew,"records");
        List<ReviewBasicDTO> data = handleReviews(page.getRecords());
        data.sort((o1, o2) -> Long.compare(o2.getId() ,o1.getId()));
        pageNew.setRecords(data);

        return PageUtil.toBean(pageNew);
    }

    @Override
    public ReviewDTO<ReviewAble> getReviewById(Long id, ReviewMetaVo metaVo) {
        EduReview eduReview = baseMapper.selectById(id);
        Assert.notNull(eduReview,"Review not found");
        ReviewDTO<ReviewAble> review = new ReviewDTO<>() ;
        BeanUtils.copyProperties(eduReview,review);

//        handleReviewContent(review, eduReview.getRefId(), eduReview.getRefType(),metaVo);
        review.setMessages(reviewMsgService.getReviewMessageById(id));


        return review;
    }

    @Override
    public ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id, ReviewMetaVo metaVo) {
        ReviewTargetDTO<ReviewAble> review = reviewServiceFactory.getService(metaVo.type()).getReviewByTargetId(id);
        List<EduReviewMsg> messages = reviewMsgService.getReviewMessageById(id);
        review.setMessage(messages);
        return review;
    }

//    private void handleReviewContent(ReviewDTO<ReviewAble> review, Long refId, ReviewType type, ReviewMetaVo metaVo) {
//            switch (type){
//                case RESEARCH -> {
//                    review.setApplied(researchService.getReviewById(refId));
//                    review.setReviewed(researchPublishedService.getReviewById(refId));
//                }
//
//                case ACTIVITY -> {
//                    review.setApplied(activityService.getReviewById(refId));
//                    review.setReviewed(activityPublishedService.getReviewById(refId));
//                }
//
//                case METHODOLOGY-> {
//
//                    review.setApplied(methodologyService.getReviewById(refId));
//                    review.setReviewed(methodologyPublishedService.getReviewById(refId));
//                }
//
//                case REPORT-> {
//
//                    review.setApplied(reportService.getReviewById(refId));
//                    review.setReviewed(reportPublishedService.getReviewById(refId));
//                }
//                default -> {
//                    review.setApplied(courseService.getReviewByIdType(refId, type, metaVo));
//                }
//            }
//
//    }


    private List<ReviewBasicDTO> handleReviews(List<EduReview> reviews) {
        List<ReviewBasicDTO> dataList = new ArrayList<>();
        Map<ReviewType, List<EduReview>> reviewTypeListMap= reviews.stream().collect(Collectors.groupingBy(EduReview::getRefType, Collectors.toList()));
        Map<ReviewType, List<EduReview>> reviewTypeCourseMap = new HashMap<>();
        reviewTypeListMap.computeIfPresent(ReviewType.COURSE, reviewTypeCourseMap::put);
        reviewTypeListMap.computeIfPresent(ReviewType.CHAPTER, reviewTypeCourseMap::put);
        reviewTypeListMap.computeIfPresent(ReviewType.SECTION, reviewTypeCourseMap::put);
        dataList.addAll(courseService.getInfoByReviews(reviewTypeCourseMap));
        dataList.addAll(activityService.getInfoByReviews(reviewTypeListMap.getOrDefault(ReviewType.ACTIVITY, Collections.emptyList())));
        dataList.addAll(researchService.getInfoByReviews(reviewTypeListMap.getOrDefault(ReviewType.RESEARCH, Collections.emptyList())));
        dataList.addAll(methodologyService.getInfoByReviews(reviewTypeListMap.getOrDefault(ReviewType.METHODOLOGY, Collections.emptyList())));
        dataList.addAll(reportService.getInfoByReviews(reviewTypeListMap.getOrDefault(ReviewType.REPORT,Collections.emptyList())));

        return dataList;
    }


    @Override
    public List<EduReview> getReviewByResearchId(Long id) {
        LambdaQueryWrapper<EduReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduReview::getRefType, ReviewType.RESEARCH)
                .eq(EduReview::getRefId, id)
                .orderByAsc(EduReview::getId);
        return baseMapper.selectList(wrapper);
    }

}
