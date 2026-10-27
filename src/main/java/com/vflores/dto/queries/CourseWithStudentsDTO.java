package com.vflores.dto.queries;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseWithStudentsDTO {

    private Integer idCourse;
    private String acronym;
    private String name;
    private List<StudentSummaryDTO> students;
}
