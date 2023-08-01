package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.enums.ReviewType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewServiceFactory {
    private final ReviewableService eduResearchServiceImp;
    private final ReviewableService eduCourseServiceImp;
    private final ReviewableService eduChapterServiceImp;
    private final ReviewableService eduSectionServiceImp;
    private final ReviewableService eduContentServiceImp;
    private final ReviewableService eduActivityServiceImp;
    private final ReviewableService eduMethodologyServiceImp;
    private final ReviewableService eduReportServiceImp;
    public ReviewableService getService(ReviewType type){
        return switch (type) {
            case RESEARCH -> eduResearchServiceImp;
            case COURSE -> eduCourseServiceImp;
            case CHAPTER -> eduChapterServiceImp;
            case SECTION -> eduSectionServiceImp;
            case SYLLABUS -> eduContentServiceImp;
            case ACTIVITY -> eduActivityServiceImp;
            case METHODOLOGY -> eduMethodologyServiceImp;
            case REPORT -> eduReportServiceImp;
        };
    }
}
