package com.vflores.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchoolTermDTO {

    private Integer idSchoolTerm;

    @NotNull
    @Min(1900)
    @Max(2200)
    private Integer year;

    @NotNull
    @Min(1)
    @Max(2)
    private Integer semester;
}
