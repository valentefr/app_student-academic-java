package com.vflores.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollDetailPKDTO {

    @NotNull
    private SchoolTermDTO schoolTerm;

    @NotNull
    private EnrollDTO enroll;

    @NotNull
    private CourseDTO course;
}
