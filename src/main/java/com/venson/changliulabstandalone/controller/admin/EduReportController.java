package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.EduReport;
import com.venson.changliulabstandalone.entity.dto.AdminReportDTO;
import com.venson.changliulabstandalone.entity.dto.AdminReportPreviewDTO;
import com.venson.changliulabstandalone.entity.enums.PageType;
import com.venson.changliulabstandalone.service.EduReportService;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author venson
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/eduservice/admin/edu-report")

public class EduReportController {

    @Autowired
    private EduReportService reportService;
    @GetMapping(value = "{current}/{size}", params = "type")
    @PreAuthorize("hasAuthority('Report.READ')")
    public ResponseEntity<PageResponse<EduReport>> getPageReport(@PathVariable int current, @PathVariable int size,
                                                                 @RequestParam(required = false) PageType type) {
        PageResponse<EduReport> page = reportService.getPageReport(current, size, type);
        return ResUtils.ok(page);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Report.READ')")
    public ResponseEntity<AdminReportDTO> getReport(@PathVariable long id){
        return ResUtils.ok(reportService.getReportById(id));
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('Report.CREATE')")
    public ResponseEntity<Long> addReport(@RequestBody  AdminReportDTO dto){
        Long id = reportService.addReport(dto);
        return ResUtils.ok();
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Report.EDIT')")
    public ResponseEntity<Long> updateReport(@PathVariable Long id,@RequestBody  AdminReportDTO dto){
        Long version = reportService.updateReport(id,dto);
        return ResUtils.ok(version);
    }
    @GetMapping("preview/{id}")
    @PreAuthorize("hasAuthority('Report.READ')")
    public ResponseEntity<AdminReportPreviewDTO> previewReport(@PathVariable Long id){
         AdminReportPreviewDTO preview = reportService.getReportPreviewById(id);
        return ResUtils.ok(preview);
    }
    @GetMapping("enable/{id}")
    @PreAuthorize("hasAuthority('Report.ENABLE')")
    public ResponseEntity<String> switchEnableReport(@PathVariable Long id){
        reportService.switchEnableById(id);
        return ResUtils.ok();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Report.REMOVE')")
    public ResponseEntity<String> removeReport(@PathVariable Long id){
        reportService.removeReport(id);
        return ResUtils.ok();
    }


}
