package com.venson.changliulabstandalone.controller.front;

import com.venson.changliulabstandalone.entity.EduReportPublished;
import com.venson.changliulabstandalone.entity.front.dto.ReportFrontDTO;
import com.venson.changliulabstandalone.service.EduReportPublishedService;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("eduservice/front/report")
public class ReportFrontController {
    @Autowired
    private EduReportPublishedService reportPublishedService;
    @GetMapping("{current}/{size}")
    public Result<PageResponse<EduReportPublished>> getPageFrontReport(@PathVariable int current, @PathVariable int size){
        PageResponse<EduReportPublished> page = reportPublishedService.getPageFrontReport(current, size);
        return Result.success(page);
    }
    @GetMapping("{id}")
    public Result<ReportFrontDTO> getReportById(@PathVariable int id){
        ReportFrontDTO dto = reportPublishedService.getFrontReportById(id);
        return Result.success(dto);
    }

}
