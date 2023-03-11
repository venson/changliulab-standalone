package com.venson.changliulabstandalone.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venson.changliulabstandalone.entity.front.dto.ResearchFrontDTO;
import com.venson.changliulabstandalone.service.front.ResearchFrontService;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.service.admin.EduResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("eduservice/front/research")
public class ResearchFrontController {

    @Autowired
    private EduResearchService service;
    @Autowired
    private ResearchFrontService researchFrontService;
    @GetMapping("{lang}")
    public Result<EduResearch> getResearch(@PathVariable LanguageEnum lang){
        LambdaQueryWrapper<EduResearch> wrapper = new QueryWrapper<EduResearch>().lambda();
        wrapper.eq(EduResearch::getLanguage,lang).eq(EduResearch::getEnable,true);
        wrapper.select(EduResearch::getPublishedHtmlBrBase64, EduResearch::getLanguage);
        EduResearch research= service.getOne(wrapper);

        return Result.success(research);
    }
    @GetMapping()
    public Result<List<ResearchFrontDTO>> getResearchList(){
        List<ResearchFrontDTO> researches = researchFrontService.getResearchList();
        return Result.success(researches);
    }
}
