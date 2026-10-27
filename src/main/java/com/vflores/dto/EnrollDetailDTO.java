package com.vflores.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollDetailDTO {

    @NotNull
    @Valid
    private EnrollDetailPKDTO idEnrollDetail;

    @NotNull
    @Size(max = 20)
    private String classRoom;
}
