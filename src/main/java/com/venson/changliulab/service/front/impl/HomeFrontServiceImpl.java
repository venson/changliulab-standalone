package com.venson.changliulab.service.front.impl;

import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.front.dto.ActivityFrontBriefDTO;
import com.venson.changliulab.entity.front.dto.CourseFrontBriefDTO;
import com.venson.changliulab.entity.front.dto.IndexFrontDTO;
import com.venson.changliulab.entity.front.dto.MemberFrontBriefDTO;
import com.venson.changliulab.service.front.ActivityFrontService;
import com.venson.changliulab.service.front.CourseFrontService;
import com.venson.changliulab.service.front.HomeFrontService;
import com.venson.changliulab.service.front.MemberFrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeFrontServiceImpl implements HomeFrontService {

    @Autowired
    private CourseFrontService courseFrontService;

    @Autowired
    private ActivityFrontService activityFrontService;

    @Autowired
    private MemberFrontService memberFrontService;
    @Override
    @Cacheable(value = FrontCacheConst.HOME_NAME,key = FrontCacheConst.HOME_HOME_KEY)
    public IndexFrontDTO getHomePage() {

        List<CourseFrontBriefDTO> courseList = courseFrontService.getFrontIndexCourse();
        List<ActivityFrontBriefDTO> activityList = activityFrontService.getFrontIndexActivity();
        List<MemberFrontBriefDTO> memberList = memberFrontService.getFrontIndexMember();



        IndexFrontDTO index = new IndexFrontDTO();
        index.setCourse(courseList);
        index.setActivity(activityList);
        index.setMember(memberList);
        return index;
    }
}
