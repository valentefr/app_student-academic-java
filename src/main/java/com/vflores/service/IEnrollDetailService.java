package com.vflores.service;

import com.vflores.dto.queries.CourseWithStudentsDTO;
import com.vflores.model.EnrollDetail;
import com.vflores.model.EnrollDetailPK;

import java.util.List;

public interface IEnrollDetailService extends ICRUD<EnrollDetail, EnrollDetailPK> {

    List<CourseWithStudentsDTO> findCoursesWithStudents(Boolean enabled, Integer year);
}
