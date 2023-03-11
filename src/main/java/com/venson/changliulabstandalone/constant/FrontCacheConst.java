package com.venson.changliulabstandalone.constant;

import org.springframework.stereotype.Component;

@Component
public final class FrontCacheConst {
    private FrontCacheConst(){}
    public static final String HOME_NAME="home";
    public static final String HOME_ACTIVITY_KEY="'activity'";
    public static final String HOME_MEMBER_KEY="'member'";
    public static final String HOME_COURSE_KEY="'course'";
    // the
    public static final String COURSE_NAME="course";
    public static final String COURSE_PAGE_NAME="course:pagination";
    public static final String COURSE_MEMBER_PAGE_NAME="course:member:pagination";
    public static final String COURSE_TOC_KEY_PREFIX="'toc:'";
    public static final String COURSE_DESC_KEY_PREFIX="'desc:'";
    public static final String COURSE_SYLLABUS_KEY_PREFIX="'syllabus:'";

    public static final String ACTIVITY_PAGE_NAME="activity:pagination:";
    public static final String ACTIVITY_NAME="activity";

    public static final String SECTION_NAME="section";


    //
    public static final String MEMBER_NAME="member";
    public static final String MEMBER_DTO_KEY_PREFIX="'dto:'";

    //Basic member pojo
    public static final String MEMBER_KEY_PREFIX="'member:'";
    public static final String MEMBER_PAGE_NAME="member:pagination";

    public static final String BANNER_NAME="banner";
    public static final String BANNER_PAGE_NAME="banner:pagination";

    public static final String SCHOLAR_NAME="scholar";
    public static final String SCHOLAR_CITATION_KEY="'citation:member:'";
    public static final String SCHOLAR_PAGE_NAME="scholar:pagination";
    public static final String SCHOLAR_MEMBER_PAGE_NAME="scholar:member:pagination";

    public static final String RESEARCH_NAME="research";
    public static final String RESEARCH_HOME_KEY="'home'";
    public static final String RESEARCH_MEMBER_KEY="'member:'";

    public static final String METHODOLOGY_NAME="methodology";
    public static final String METHODOLOGY_PAGE_NAME="methodology:pagination";

    public static final String USE_NAME="user:info";
    public static final String USE_ADMIN_KEY="'admin:'";
    public static final String USE_FRONT_KEY="'front:'";


}
