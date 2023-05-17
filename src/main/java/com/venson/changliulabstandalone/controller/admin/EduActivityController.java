package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduActivity;
import com.venson.changliulabstandalone.entity.dto.ActivityAdminDTO;
import com.venson.changliulabstandalone.entity.dto.ActivityPreviewDTO;
import com.venson.changliulabstandalone.entity.enums.PageType;
import com.venson.changliulabstandalone.service.admin.EduActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
@RestController
@RequestMapping("/eduservice/admin/edu-activity")
@RequiredArgsConstructor
public class EduActivityController {

    private final EduActivityService activityService;


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Activity.READ')")
    public ResponseEntity<ActivityAdminDTO> getActivity(@PathVariable Long id){
        ActivityAdminDTO activity = activityService.getActivityById(id);
        return ResUtils.ok(activity);
    }

//    @PostMapping(value = "{page}/{limit}", params={"type", "title", "begin", "end"})
    @GetMapping(value = "{page}/{limit}", params={"type"})
    @PreAuthorize("hasAuthority('Activity.READ')")
    public ResponseEntity<PageResponse<EduActivity>> getPageActivityList(@PathVariable Integer page,
                                      @PathVariable Integer limit,
                                                                 @RequestParam PageType type,
                                                                 @RequestParam(required = false) String title,
                                                                 @RequestParam(required = false) String begin,
                                                                 @RequestParam(required = false) String end){
        PageResponse<EduActivity> pageRes;
        if( type == PageType.REVIEW){
            pageRes = activityService.getPageReviewList(page,limit);
        }else{
            pageRes = activityService.getPageActivityList(page, limit, title, begin, end);
        }
        return ResUtils.ok(pageRes);
    }
//    @PostMapping()
//    @PreAuthorize("hasAuthority('Activity.CREATE')")
//    public ResponseEntity<Long> addActivity(@Valid @RequestBody ActivityAdminDTO activityAdminDTO){
//        Long id = activityService.addActivity(activityAdminDTO);
//        return ResUtils.ok(id);
//    }
    @GetMapping()
    @PreAuthorize("hasAuthority('Activity.READ')")
    public ResponseEntity<PageResponse<EduActivity>> getPage(PageQueryVo vo){
        PageResponse<EduActivity> page = activityService.getPageActivityList(vo);
        return ResUtils.ok(page);
    }
    @PostMapping()
    @PreAuthorize("hasAuthority('Activity.CREATE')")
    public ResponseEntity<Long> addActivity(@Valid @RequestBody ActivityAdminDTO activityAdminDTO){
        Long id = activityService.addActivity(activityAdminDTO);
        return ResUtils.created(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Activity.EDIT')")
    public ResponseEntity<String> updateActivity(@PathVariable Long id,
                                 @Valid @RequestBody ActivityAdminDTO activityAdminDTO){
        activityService.updateActivity(id, activityAdminDTO);
        return ResUtils.ok();
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Activity.REMOVE')")
    public ResponseEntity<String> deleteActivity(@PathVariable Long id){
        activityService.deleteActivity(id);
        return ResUtils.ok();
    }
    @GetMapping("review/{page}/{limit}")
    @PreAuthorize("hasAuthority('Activity.review')")
    public ResponseEntity<PageResponse<EduActivity>> getPageRequestList(@PathVariable Integer page,
                                                   @PathVariable Integer limit){
        PageResponse<EduActivity> pageRes = activityService.getPageReviewList(page, limit);
        return ResUtils.ok(pageRes);
    }
    @GetMapping("preview/{id}")
    public ResponseEntity<ActivityPreviewDTO> getActivityPreview(@PathVariable long id){
        ActivityPreviewDTO preview = activityService.getPreviewByActivityId(id);
        return ResUtils.ok(preview);
    }
    @PutMapping("enable/{id}")
    @PreAuthorize("hasAuthority('Activity.ENABLE')")
    public ResponseEntity<String> switchActivityById(@PathVariable Long id){
        activityService.switchEnableByActivityId(id);
        return ResUtils.ok();
    }



}
