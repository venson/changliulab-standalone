package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.entity.pojo.EduScholarCitation;
import com.venson.changliulab.mapper.EduScholarCitationMapper;
import com.venson.changliulab.service.admin.EduScholarCitationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
public class EduScholarCitationServiceImp extends ServiceImpl<EduScholarCitationMapper, EduScholarCitation> implements EduScholarCitationService {

    @Override
    public List<EduScholarCitation> getCitationsByScholarId(Long id) {
        LambdaQueryWrapper<EduScholarCitation> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduScholarCitation::getId, EduScholarCitation::getYear, EduScholarCitation::getCitations)
                .eq(EduScholarCitation::getScholarId,id);
        return baseMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void updateScholarCitation(List<EduScholarCitation> newCitationList, Long scholarId) {
        LambdaQueryWrapper<EduScholarCitation> searchWrapper = new LambdaQueryWrapper<>();
//        HashMap<Long, EduScholarCitation> oldCitationMap = new HashMap<>();
        searchWrapper.eq(EduScholarCitation::getScholarId,scholarId);
        List<EduScholarCitation> oldCitationList = baseMapper.selectList(searchWrapper);
        List<EduScholarCitation> updateList = new ArrayList<>();
        List<EduScholarCitation> addList= new ArrayList<>();
        List<Long> removeList;

        Map<Long, EduScholarCitation> oldCitationMap = oldCitationList.stream().collect(Collectors.toMap(EduScholarCitation::getId, o -> o));
//        oldCitationList.forEach(o->oldCitationMap.put(o.getId(),o));
        newCitationList.forEach(o-> {
            if(o.getId()!=null && oldCitationMap.containsKey(o.getId())){
                updateList.add(o);
                oldCitationMap.remove(o.getId());
            } else{
                o.setScholarId(scholarId);
                addList.add(o);
            }
        });
        if(!CollectionUtils.isEmpty(oldCitationMap)){
            removeList = new ArrayList<>(oldCitationMap.keySet());
            baseMapper.deleteBatchIds(removeList);
        }

        if(!CollectionUtils.isEmpty(updateList))this.updateBatchById(updateList);
        if(!CollectionUtils.isEmpty(addList))this.saveBatch(addList);

    }

    @Override
    public void addScholarCitation(List<EduScholarCitation> citationList, Long id) {
        if(citationList.size()>0){
            citationList.forEach(o->o.setScholarId(id));
            saveBatch(citationList);
        }



    }
}
