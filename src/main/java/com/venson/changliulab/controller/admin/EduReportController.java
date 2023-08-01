package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.pojo.EduReport;
import com.venson.changliulab.entity.dto.AdminReportDTO;
import com.venson.changliulab.entity.dto.AdminReportPreviewDTO;
import com.venson.changliulab.entity.enums.PageType;
import com.venson.changliulab.service.EduReportService;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.ResUtils;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EduReportController {

    private final EduReportService reportService;
    @GetMapping(value = "{current}/{size}", params = "type")
    @PreAuthorize("hasAuthority('Report.READ')")
    public ResponseEntity<PageResponse<EduReport>> getPageReport(@PathVariable int current, @PathVariable int size,
                                                                 @RequestParam(required = false) PageType type) {
        PageResponse<EduReport> page = reportService.getPageReport(current, size, type);
        return ResponseEntity.ok(page);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Report.READ')")
    public ResponseEntity<AdminReportDTO> getReport(@PathVariable long id){
        return ResponseEntity.ok(reportService.getReportById(id));
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('Report.CREATE')")
    public ResponseEntity<Long> addReport(@RequestBody  AdminReportDTO dto){
        Long id = reportService.addReport(dto);
        return ResponseEntity.ok().build();
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Report.EDIT')")
    public ResponseEntity<Long> updateReport(@PathVariable Long id,@RequestBody  AdminReportDTO dto){
        Long version = reportService.updateReport(id,dto);
        return ResponseEntity.ok(version);
    }
    @GetMapping("preview/{id}")
    @PreAuthorize("hasAuthority('Report.READ')")
    public ResponseEntity<AdminReportPreviewDTO> previewReport(@PathVariable Long id){
         AdminReportPreviewDTO preview = reportService.getReportPreviewById(id);
        return ResponseEntity.ok(preview);
    }
    @GetMapping("enable/{id}")
    @PreAuthorize("hasAuthority('Report.ENABLE')")
    public ResponseEntity<String> switchEnableReport(@PathVariable Long id){
        reportService.switchEnableById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Report.REMOVE')")
    public ResponseEntity<String> removeReport(@PathVariable Long id){
        reportService.removeReport(id);
        return ResponseEntity.ok().build();
    }


}
