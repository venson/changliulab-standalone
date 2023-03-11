package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.vo.admin.AdminMemberVo;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.dto.MemberQuery;
import com.venson.changliulabstandalone.entity.vo.MemberVo;
import com.venson.changliulabstandalone.service.admin.EduMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-05-02
 */
@Slf4j
@RestController
@RequestMapping("/eduservice/admin/edu-member")
public class EduMemberController {

    private final EduMemberService memberService;

    @Autowired
    public EduMemberController(EduMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public Result<List<EduMember>> getAllMember(@RequestParam(required = false) String type){
        List<EduMember> list;
        // get current member when type has context
        if(StringUtils.hasText(type)){
            list = memberService.getCurrentMember();
        }else{
            list  = memberService.getAllMember();
        }
        return Result.success(list);

    }

    @DeleteMapping("{id}")
    public Result<String> removeMember(@PathVariable("id") Long id){
        boolean result = memberService.removeById(id);
        if (result){
            return Result.success();
        } else return Result.error();
    }


//    @GetMapping("{pageNum}/{limit}")
//    public Result<PageResponse<EduMember>> memberPageList(@PathVariable Integer pageNum,
//                                                          @PathVariable Integer limit){
//        return Result.success(pageRes);
//
//    }


    @PostMapping("{pageNum}/{limit}")
    public Result<PageResponse<EduMember>> pageMemberCondition(@PathVariable Integer pageNum,
                                      @PathVariable Integer limit,
                                      @RequestBody(required = false)  MemberQuery memberQuery){
        PageResponse<EduMember> pageRes = memberService.getMemberPage(pageNum, limit, memberQuery);
        return Result.success(pageRes);

    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('member.add')")
    public Result<String> addMember(@RequestBody AdminMemberVo eduMember){
        memberService.addMember(eduMember);
        return Result.success();
    }

    @GetMapping("{id}")
    public Result<EduMember> getMember(@PathVariable Long id){
        EduMember member = memberService.getById(id);
        return Result.success( member);
    }



    @PutMapping("{id}")
    public Result<String> updateMember(@PathVariable Long id,
                               @RequestBody MemberVo member){
        memberService.updateMember(id,member);
        return Result.success();
    }



}
