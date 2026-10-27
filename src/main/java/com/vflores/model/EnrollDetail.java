package com.vflores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class EnrollDetail {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private EnrollDetailPK idEnrollDetail;

    @Column(nullable = false,  length = 20)
    private String classRoom;
}
