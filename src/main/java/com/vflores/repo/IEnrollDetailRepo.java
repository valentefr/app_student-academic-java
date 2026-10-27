package com.vflores.repo;

import com.vflores.dto.queries.CourseStudentDTO;
import com.vflores.model.EnrollDetail;
import com.vflores.model.EnrollDetailPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEnrollDetailRepo extends IGenericRepo<EnrollDetail, EnrollDetailPK> {

    @Query("""
            SELECT DISTINCT new com.vflores.dto.queries.CourseStudentDTO(c.idCourse, c.acronym, c.name, s.DNI, s.firstName, s.lastName)
            FROM EnrollDetail ed
            JOIN ed.idEnrollDetail.course c
            JOIN ed.idEnrollDetail.enroll e
            JOIN e.student s
            JOIN ed.idEnrollDetail.schoolTerm st
            WHERE (:enabled IS NULL OR c.enabled = :enabled)
              AND (:year IS NULL OR st.year = :year)
            ORDER BY c.name, c.idCourse, s.lastName, s.firstName
            """)
    List<CourseStudentDTO> findCourseStudents(@Param("enabled") Boolean enabled,
                                              @Param("year") Integer year);
}
