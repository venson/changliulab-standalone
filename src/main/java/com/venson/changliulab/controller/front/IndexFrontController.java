package com.venson.changliulab.controller.front;

import com.venson.changliulab.entity.front.dto.IndexFrontDTO;
import com.venson.changliulab.service.front.HomeFrontService;
import com.venson.changliulab.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for front main page
 */
@RestController
@RequestMapping("eduservice/front/index")
public class IndexFrontController {
    @Autowired
    private HomeFrontService homeFrontService;


    @GetMapping()
    public Result<IndexFrontDTO> index(){
        return Result.success(homeFrontService.getHomePage());


    }
}
