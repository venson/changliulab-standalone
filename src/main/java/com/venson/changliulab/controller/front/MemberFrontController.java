package com.venson.changliulab.controller.front;

import com.venson.changliulab.entity.front.dto.MemberFrontDTO;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.Result;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.enums.MemberLevel;
import com.venson.changliulab.service.front.MemberFrontService;
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
