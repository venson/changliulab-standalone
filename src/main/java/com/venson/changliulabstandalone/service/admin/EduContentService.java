package com.venson.changliulabstandalone.service.admin;

import com.venson.changliulabstandalone.entity.dto.CourseSyllabusDTO;

import java.util.List;

public interface EduContentService {
    List<CourseSyllabusDTO> getSyllabusByCourseId(Long courseId);
}
