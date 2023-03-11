package com.venson.changliulabstandalone.service.admin;

import com.venson.changliulabstandalone.entity.pojo.EduScholarCitation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-06-20
 */
public interface EduScholarCitationService extends IService<EduScholarCitation> {

    List<EduScholarCitation> getCitationsByScholarId(Long id);

    void updateScholarCitation(List<EduScholarCitation> citationList, Long scholar);

    void addScholarCitation(List<EduScholarCitation> citationList, Long id);
}
