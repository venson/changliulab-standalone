package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.dto.CourseSyllabusDTO;

import java.util.List;

public interface EduContentService {
    List<CourseSyllabusDTO> getSyllabusByCourseId(Long courseId);

    List<CourseSyllabusDTO> getReviewedSyllabusByCourseId(Long courseId);


}
