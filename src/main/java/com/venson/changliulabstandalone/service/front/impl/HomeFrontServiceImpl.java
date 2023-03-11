package com.venson.changliulabstandalone.service.front.impl;

import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.front.dto.ActivityFrontBriefDTO;
import com.venson.changliulabstandalone.entity.front.dto.CourseFrontBriefDTO;
import com.venson.changliulabstandalone.entity.front.dto.IndexFrontDTO;
import com.venson.changliulabstandalone.entity.front.dto.MemberFrontBriefDTO;
import com.venson.changliulabstandalone.service.front.ActivityFrontService;
import com.venson.changliulabstandalone.service.front.CourseFrontService;
import com.venson.changliulabstandalone.service.front.HomeFrontService;
import com.venson.changliulabstandalone.service.front.MemberFrontService;
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
    @Cacheable(value = FrontCacheConst.HOME_NAME,key = FrontCacheConst.HOME_NAME)
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
