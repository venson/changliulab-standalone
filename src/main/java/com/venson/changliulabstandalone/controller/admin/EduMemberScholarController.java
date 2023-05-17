package com.venson.changliulabstandalone.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduMemberScholar;
import com.venson.changliulabstandalone.service.admin.EduMemberScholarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<EduMemberScholar>> getMemberIdList(@PathVariable Long scholarId){
        List<EduMemberScholar> list = memberScholarService.getMembersByScholarId(scholarId);
        return ResUtils.ok( list);
    }
    @PostMapping("{scholarId}")
    public ResponseEntity<String> newScholarMember(@PathVariable Long scholarId, @RequestBody List<EduMemberScholar> memberList){
        memberList.forEach(o -> o.setScholarId(scholarId));
        memberScholarService.saveBatch(memberList);
        return ResUtils.ok();
    }
    @PutMapping("{scholarId}")
    public ResponseEntity<String> updateMember(@PathVariable Long scholarId, @RequestBody List<EduMember> memberList){
        memberScholarService.updateMemberScholar(scholarId, memberList);
        return ResUtils.ok();
    }

    @DeleteMapping("{scholarId}")
    public ResponseEntity<String> deleteMemberByScholarId(@PathVariable String scholarId){
        QueryWrapper<EduMemberScholar> wrapper = new QueryWrapper<>();
        wrapper.eq("scholar_id", scholarId);
        memberScholarService.remove(wrapper);
        return ResUtils.ok();
    }
    @DeleteMapping("member")
    public ResponseEntity<String> deleteMemberByMemberId(@RequestBody List<String> memberIdList){
        memberScholarService.removeBatchByIds(memberIdList);
        return ResUtils.ok();
    }

}
