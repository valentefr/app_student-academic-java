package com.vflores.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollDTO {

    private Integer idEnroll;

    @NotNull
    private StudentDTO student;

    @NotNull
    private LocalDate date;

    @NotNull
    private Boolean enabled;
}
