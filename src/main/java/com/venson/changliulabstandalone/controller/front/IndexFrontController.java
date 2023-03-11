package com.venson.changliulabstandalone.controller.front;

import com.venson.changliulabstandalone.entity.front.dto.IndexFrontDTO;
import com.venson.changliulabstandalone.service.front.HomeFrontService;
import com.venson.changliulabstandalone.utils.Result;
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
