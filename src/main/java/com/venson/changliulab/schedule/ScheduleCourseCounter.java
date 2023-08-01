package com.venson.changliulab.schedule;

import com.venson.changliulab.entity.pojo.EduCourse;
import com.venson.changliulab.entity.pojo.EduCoursePublished;
import com.venson.changliulab.service.admin.EduCoursePublishedService;
import com.venson.changliulab.service.admin.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@EnableScheduling
@Configuration
public class ScheduleCourseCounter {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduCoursePublishedService coursePublishedService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Scheduled(cron = "@hourly")
    public void saveViewCounters(){
        String prefix= "course:counter:";
        Set<String> keys = stringRedisTemplate.keys(prefix +"*");
        if(keys ==null || keys.size() ==0){
            return;
        }
        List<String> keyList = keys.stream().toList();
        List<String> counters = stringRedisTemplate.opsForValue().multiGet(keyList);
        if(counters ==null ||counters.size() ==0){
            return;
        }
        stringRedisTemplate.delete(keys);
        LinkedList<EduCourse> courseList = new LinkedList<>();
        LinkedList<EduCoursePublished> coursePublishedList = new LinkedList<>();
        for (int i = 0; i < keys.size(); i++) {
            EduCourse courseTemp = new EduCourse();
            Long id = Long.valueOf(keyList.get(i).substring(prefix.length()));
            Long count = Long.valueOf(counters.get(i));
            courseTemp.setId(id);
            courseTemp.setViewCount(count);
            courseList.addLast(courseTemp);
            EduCoursePublished coursePublishedTemp = new EduCoursePublished();
            coursePublishedTemp.setId(id);
            coursePublishedTemp.setViewCount(count);
            coursePublishedList.addLast(coursePublishedTemp);
        }
        courseService.updateBatchById(courseList);
        coursePublishedService.updateBatchById(coursePublishedList);


    }
}
