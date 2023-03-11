package com.venson.changliulabstandalone.controller.front;

import com.venson.changliulabstandalone.entity.front.dto.MemberFrontDTO;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.enums.MemberLevel;
import com.venson.changliulabstandalone.service.front.MemberFrontService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/front/member")
@Slf4j
public class MemberFrontController {

    @Autowired
    private MemberFrontService memberFrontService;


//    @GetMapping()
//    public Result<List<EduMember>> getPIMemberFront(){
//        List<EduMember> PIMembers = memberFrontService.getPIMemberFront();
//        return Result.success(PIMembers);
//    }


    @GetMapping(value = "{page}/{limit}", params = {"level"})
    public Result<PageResponse<EduMember>> getMemberFrontList(@PathVariable Integer page, @PathVariable Integer limit,
                                                              @RequestParam MemberLevel level){
        PageResponse<EduMember> pageRes = memberFrontService.getPageFrontMemberListByLevel(page, limit, level);
        return Result.success(pageRes);
    }

    @GetMapping(value = "{id}", params={"sPage", "cPage", "sSize", "cSize"})
    public Result<MemberFrontDTO> getMemberFrontByID(@PathVariable Long id,
                                                     @RequestParam Integer sPage, @RequestParam Integer cPage,
                                                     @RequestParam Integer sSize, @RequestParam Integer cSize
    ){

        MemberFrontDTO member = memberFrontService.getMemberFrontDTOByID(id, sPage, cPage, sSize, cSize);
        return Result.success(member);
    }


}
