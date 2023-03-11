package com.venson.changliulabstandalone.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduMemberScholar;
import com.venson.changliulabstandalone.service.admin.EduMemberScholarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author venson
 * @since 2022-06-20
 */
@RestController
@RequestMapping("/eduservice/admin/edu-member-scholar")
public class EduMemberScholarController {
    @Autowired
    private EduMemberScholarService memberScholarService;

    @GetMapping("{scholarId}")
    public Result<List<EduMemberScholar>> getMemberIdList(@PathVariable Long scholarId){
        List<EduMemberScholar> list = memberScholarService.getMembersByScholarId(scholarId);
        return Result.success( list);
    }
    @PostMapping("{scholarId}")
    public Result<String> newScholarMember(@PathVariable Long scholarId, @RequestBody List<EduMemberScholar> memberList){
        memberList.forEach(o -> o.setScholarId(scholarId));
        memberScholarService.saveBatch(memberList);
        return Result.success();
    }
    @PutMapping("{scholarId}")
    public Result<String> updateMember(@PathVariable Long scholarId, @RequestBody List<EduMember> memberList){
        memberScholarService.updateMemberScholar(scholarId, memberList);
        return Result.success();
    }

    @DeleteMapping("{scholarId}")
    public Result<String> deleteMemberByScholarId(@PathVariable String scholarId){
        QueryWrapper<EduMemberScholar> wrapper = new QueryWrapper<>();
        wrapper.eq("scholar_id", scholarId);
        memberScholarService.remove(wrapper);
        return Result.success();
    }
    @DeleteMapping("member")
    public Result<String> deleteMemberByMemberId(@RequestBody List<String> memberIdList){
        memberScholarService.removeBatchByIds(memberIdList);
        return Result.success();
    }

}
