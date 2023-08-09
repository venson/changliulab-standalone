package com.venson.changliulab.controller.front;

import com.venson.changliulab.entity.front.dto.ResearchFrontDTO;
import com.venson.changliulab.service.front.ResearchFrontService;
import com.venson.changliulab.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("eduservice/front/research")
@RequiredArgsConstructor
public class ResearchFrontController {

    private final ResearchFrontService researchFrontService;
    @GetMapping()
    public Result<List<ResearchFrontDTO>> getResearchList(){
        List<ResearchFrontDTO> researches = researchFrontService.getResearchList();
        return Result.success(researches);
    }
}
