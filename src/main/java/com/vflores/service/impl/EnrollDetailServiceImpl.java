package com.vflores.service.impl;

import com.vflores.dto.queries.CourseStudentDTO;
import com.vflores.dto.queries.CourseWithStudentsDTO;
import com.vflores.dto.queries.StudentSummaryDTO;
import com.vflores.model.EnrollDetail;
import com.vflores.model.EnrollDetailPK;
import com.vflores.repo.IEnrollDetailRepo;
import com.vflores.repo.IGenericRepo;
import com.vflores.service.IEnrollDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollDetailServiceImpl extends CRUDImpl<EnrollDetail, EnrollDetailPK> implements IEnrollDetailService {

    private final IEnrollDetailRepo repo;

    @Override
    protected IGenericRepo<EnrollDetail, EnrollDetailPK> getRepo() {
        return repo;
    }

    @Override
    public List<CourseWithStudentsDTO> findCoursesWithStudents(Boolean enabled, Integer year) {
        Map<Integer, CourseWithStudentsDTO> courses = new LinkedHashMap<>();

        for (CourseStudentDTO row : repo.findCourseStudents(enabled, year)) {
            CourseWithStudentsDTO course = courses.computeIfAbsent(row.getIdCourse(),
                    id -> new CourseWithStudentsDTO(id, row.getAcronym(), row.getName(), new ArrayList<>()));
            course.getStudents().add(new StudentSummaryDTO(row.getDni(), row.getFirstName(), row.getLastName()));
        }

        return new ArrayList<>(courses.values());
    }
}
