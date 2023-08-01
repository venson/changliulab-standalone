package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.pojo.EduMemberScholar;
import com.venson.changliulab.mapper.EduMemberScholarMapper;
import com.venson.changliulab.service.admin.EduMemberScholarService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-06-20
 */
@Service
public class EduMemberScholarServiceImp extends ServiceImpl<EduMemberScholarMapper, EduMemberScholar> implements EduMemberScholarService {



    @Override
    public void updateMemberScholar(Long scholarId, List<EduMember> memberList) {

        // get the currentMemberList from the database before update
        List<EduMemberScholar> currentMemberList = getMembersByScholarId(scholarId);
        List<Long> memberIdListDb = currentMemberList.stream()
                .map(EduMemberScholar::getId).toList();
        List<Long> newMemberIdList = memberList.stream()
                .map(EduMember::getId).toList();
        List<EduMemberScholar> newBatch;
        List<Long> removeBatch;
        if(ObjectUtils.isEmpty(currentMemberList)){
            newBatch = memberList.stream().map(o -> new EduMemberScholar(o.getId(),o.getName(),scholarId))
                    .filter(o -> !memberIdListDb.contains(o.getId())).collect(Collectors.toList());
            removeBatch = null;
        }else{
            removeBatch = currentMemberList.stream().map(EduMemberScholar::getId)
                    .filter(id -> !newMemberIdList.contains(id)).collect(Collectors.toList());

            newBatch = memberList.stream().map(o -> new EduMemberScholar(o.getId(),o.getName(),scholarId))
                            .filter(o -> !memberIdListDb.contains(o.getId())).collect(Collectors.toList());

        }

        // the IDs in the currentMemberList that are not in newMemberIdList should be deleted from the database

        if(newBatch.size()>0){
            this.saveBatch(newBatch);
        }
        if(removeBatch!=null && removeBatch.size()>0){
            this.removeBatchByIds(removeBatch);
        }

    }

    @Override
    public List<EduMemberScholar> getMembersByScholarId(Long scholarId) {
        LambdaQueryWrapper<EduMemberScholar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduMemberScholar::getScholarId,scholarId)
                .select(EduMemberScholar::getId,EduMemberScholar::getMemberId,EduMemberScholar::getScholarId,EduMemberScholar::getName);
        return baseMapper.selectList(wrapper);
    }

//    @Override
//    public void saveMemberScholar(Long scholarId, List<EduMemberScholar> memberList) {
//        memberList.forEach(o-> o.setScholarId(scholarId));
//
//    }

    @Override
    public List<Long> getScholarIdListByMemberId(Long memberId) {
        LambdaQueryWrapper<EduMemberScholar> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(EduMemberScholar::getMemberId, memberId).select(EduMemberScholar::getScholarId);
        List<EduMemberScholar> eduMemberScholars = baseMapper.selectList(wrapper);
        return eduMemberScholars.stream().map(EduMemberScholar::getScholarId).toList();
    }

    @Override
    public List<Long> getMemberIdsByScholarId(Long id) {
        LambdaQueryWrapper<EduMemberScholar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduMemberScholar::getScholarId, id).select(EduMemberScholar::getMemberId);
        return this.listObjs(wrapper,(o)-> Long.valueOf(o.toString()));
    }

    @Override
    @Transactional
    @CacheEvict(value = FrontCacheConst.SCHOLAR_NAME, key = "#scholarId")
    public void updateMemberScholarByMemberIdList(Long scholarId, List<Long> memberIdList, List<EduMember> memberList) {
        // if memberId list is not empty, but the EduMemberScholar list is empty, the member information of scholar is not updated.
        if(!CollectionUtils.isEmpty(memberIdList) && CollectionUtils.isEmpty(memberList)){
            return;
        }
        LambdaQueryWrapper<EduMemberScholar> searchWrapper = new LambdaQueryWrapper<>();
        searchWrapper.eq(EduMemberScholar::getScholarId,scholarId).select(EduMemberScholar::getMemberId,EduMemberScholar::getId);
        List<EduMemberScholar> memberScholarDbList = baseMapper.selectList(searchWrapper);
        List<EduMemberScholar> addList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(memberScholarDbList)) {
            List<Long> removeList = new ArrayList<>();
            Map<Long, EduMember> memberMap = memberList.stream().collect(Collectors.toMap(EduMember::getId, o -> o));
            Iterator<EduMemberScholar> iterator =memberScholarDbList.iterator();
            while(iterator.hasNext()){
                EduMemberScholar dbMember = iterator.next();
                if(memberMap.containsKey(dbMember.getMemberId())){
                    memberMap.remove(dbMember.getMemberId());
                    iterator.remove();
                }else{
                    removeList.add(dbMember.getId());
                }
            }
             memberMap.values().forEach(o-> addList.add(new EduMemberScholar(o.getId(),o.getName(),scholarId)));

            if( removeList.size()>0)this.removeBatchByIds(removeList);
            if(addList.size()>0)this.saveBatch(addList);

        }else{
            memberList.forEach(o-> addList.add(new EduMemberScholar(o.getId(),o.getName(),scholarId)));
            this.saveBatch(addList);
        }


    }

    @Override
    @CacheEvict(value = FrontCacheConst.SCHOLAR_PAGE_NAME, allEntries = true)
    @Transactional
    public void addMemberScholarByMemberIdList(Long id, List<EduMember> memberList) {
        List<EduMemberScholar> addList = memberList.stream().map(member -> new EduMemberScholar(member.getId(), member.getName(), id)).collect(Collectors.toList());
        this.saveBatch(addList);

    }
}
