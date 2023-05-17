package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.vo.admin.AdminMemberPageVo;
import com.venson.changliulabstandalone.entity.vo.admin.AdminMemberVo;
import com.venson.changliulabstandalone.entity.vo.admin.ListQueryParams;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.dto.MemberQuery;
import com.venson.changliulabstandalone.entity.vo.MemberVo;
import com.venson.changliulabstandalone.service.admin.EduMemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class EduMemberController {

    private final EduMemberService memberService;


    @GetMapping("members")
    public ResponseEntity<List<EduMember>> getAllMember(@RequestParam(required = false) String type){
        List<EduMember> list;
        // get current member when type has context
        if(StringUtils.hasText(type)){
            list = memberService.getCurrentMember();
        }else{
            list  = memberService.getAllMember();
        }
        return ResUtils.ok(list);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> removeMember(@PathVariable("id") Long id){
        boolean result = memberService.removeById(id);
        if (result){
            return ResUtils.ok();
        } else return ResUtils.notFount();
    }


//    @GetMapping("{pageNum}/{limit}")
//    public ResponseEntity<PageResponse<EduMember>> memberPageList(@PathVariable Integer pageNum,
//                                                          @PathVariable Integer limit){
//        return ResUtils.ok(pageRes);
//
//    }


    @PostMapping("{pageNum}/{limit}")
    @PreAuthorize("hasAuthority('Member.READ')")
    public ResponseEntity<PageResponse<EduMember>> pageMemberCondition(@PathVariable Integer pageNum,
                                      @PathVariable Integer limit,
                                      @RequestBody(required = false)  MemberQuery memberQuery){
        PageResponse<EduMember> pageRes = memberService.getMemberPage(pageNum, limit, memberQuery);
        return ResUtils.ok(pageRes);

    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('Member.READ')")
    public ResponseEntity<PageResponse<EduMember>> getPageMember(ListQueryParams params){
        PageResponse<EduMember> pageRes = memberService.getMemberPage(params);
        return ResUtils.ok(pageRes);

    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('Member.CREATE')")
    public ResponseEntity<Long> addMember(@RequestBody AdminMemberVo eduMember){
        Long id = memberService.addMember(eduMember);
        return ResUtils.created(id);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Member.READ')")
    public ResponseEntity<EduMember> getMember(@PathVariable Long id){
        EduMember member = memberService.getMemberById(id);
        return ResUtils.optional( member);
    }



    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Member.EDIT')")
    public ResponseEntity<String> updateMember(@PathVariable Long id,
                               @RequestBody MemberVo member){
        memberService.updateMember(id,member);
        return ResUtils.ok();
    }



}
