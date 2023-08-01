package com.venson.changliulab.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venson.changliulab.utils.ResUtils;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.pojo.EduMemberScholar;
import com.venson.changliulab.service.admin.EduMemberScholarService;
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
        return ResponseEntity.ok(list);
    }
    @PostMapping("{scholarId}")
    public ResponseEntity<String> newScholarMember(@PathVariable Long scholarId, @RequestBody List<EduMemberScholar> memberList){
        memberList.forEach(o -> o.setScholarId(scholarId));
        memberScholarService.saveBatch(memberList);
        return ResponseEntity.ok().build();
    }
    @PutMapping("{scholarId}")
    public ResponseEntity<String> updateMember(@PathVariable Long scholarId, @RequestBody List<EduMember> memberList){
        memberScholarService.updateMemberScholar(scholarId, memberList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{scholarId}")
    public ResponseEntity<String> deleteMemberByScholarId(@PathVariable String scholarId){
        QueryWrapper<EduMemberScholar> wrapper = new QueryWrapper<>();
        wrapper.eq("scholar_id", scholarId);
        memberScholarService.remove(wrapper);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("member")
    public ResponseEntity<String> deleteMemberByMemberId(@RequestBody List<String> memberIdList){
        memberScholarService.removeBatchByIds(memberIdList);
        return ResponseEntity.ok().build();
    }

}
