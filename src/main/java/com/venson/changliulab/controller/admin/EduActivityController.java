package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.ResUtils;
import com.venson.changliulab.entity.pojo.EduActivity;
import com.venson.changliulab.entity.dto.ActivityAdminDTO;
import com.venson.changliulab.entity.dto.ActivityPreviewDTO;
import com.venson.changliulab.entity.enums.PageType;
import com.venson.changliulab.service.admin.EduActivityService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ActivityAdminDTO> getActivity(@PathVariable Long id, CommonMetaVo vo){
        ActivityAdminDTO activity = activityService.getActivityById(id, vo);
        return ResponseEntity.ok(activity);
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
        return ResponseEntity.ok(pageRes);
    }
//    @PostMapping()
//    @PreAuthorize("hasAuthority('Activity.CREATE')")
//    public ResponseEntity<Long> addActivity(@Valid @RequestBody ActivityAdminDTO activityAdminDTO){
//        Long id = activityService.addActivity(activityAdminDTO);
//        return ResponseEntity.ok(id);
//    }
    @GetMapping()
    @PreAuthorize("hasAuthority('Activity.READ')")
    public ResponseEntity<PageResponse<EduActivity>> getPage(PageQueryVo vo){
        PageResponse<EduActivity> page = activityService.getPageActivityList(vo);
        return ResponseEntity.ok(page);
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
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Activity.REMOVE')")
    public ResponseEntity<String> deleteActivity(@PathVariable Long id){
        activityService.deleteActivity(id);
        return ResponseEntity.ok().build();
    }
//    @GetMapping("review/{page}/{limit}")
//    @PreAuthorize("hasAuthority('Activity.review')")
//    public ResponseEntity<PageResponse<EduActivity>> getPageRequestList(@PathVariable Integer page,
//                                                   @PathVariable Integer limit){
//        PageResponse<EduActivity> pageRes = activityService.getPageReviewList(page, limit);
//        return ResponseEntity.ok(pageRes);
//    }
//    @GetMapping("preview/{id}")
//    public ResponseEntity<ActivityPreviewDTO> getActivityPreview(@PathVariable long id){
//        ActivityPreviewDTO preview = activityService.getPreviewByActivityId(id);
//        return ResponseEntity.ok(preview);
//    }
    @PutMapping("enable/{id}")
    @PreAuthorize("hasAuthority('Activity.ENABLE')")
    public ResponseEntity<String> switchActivityById(@PathVariable Long id){
        activityService.switchEnableByActivityId(id);
        return ResponseEntity.ok().build();
    }



}
