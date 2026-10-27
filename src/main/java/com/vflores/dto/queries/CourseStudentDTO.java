package com.vflores.dto.queries;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseStudentDTO {
    private Integer idCourse;
    private String acronym;
    private String name;
    private String dni;
    private String firstName;
    private String lastName;
}
