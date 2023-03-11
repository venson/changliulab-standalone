package com.venson.changliulabstandalone.controller.front;

import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduActivityPublished;
import com.venson.changliulabstandalone.entity.front.dto.ActivityFrontDTO;
import com.venson.changliulabstandalone.service.front.ActivityFrontService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("eduservice/front/activity")
public class ActivityFrontController {

    private final ActivityFrontService activityFrontService;

    public ActivityFrontController(ActivityFrontService activityFrontService) {
        this.activityFrontService = activityFrontService;
    }

    @GetMapping("{page}/{limit}")
    public Result<PageResponse<EduActivityPublished>> getPageActivity(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<EduActivityPublished> pageRes = activityFrontService.getPageActivity(page, limit);
        return Result.success(pageRes);
    }
    @GetMapping("{id}")
    public Result<ActivityFrontDTO> getActivityById(@PathVariable Long id){
        ActivityFrontDTO activity = activityFrontService.getActivityById(id);
        return Result.success(activity);
    }
}
