package com.vflores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollDetailPK {

    @ManyToOne
    @JoinColumn(name = "id_school_term", nullable = false, foreignKey = @ForeignKey(name = "fk_enroll_school_term"))
    private SchoolTerm schoolTerm;

    @ManyToOne
    @JoinColumn(name = "id_enrolled", nullable = false, foreignKey = @ForeignKey(name = "fk_enroll_detail_enroll"))
    @ToString.Exclude
    private Enroll enroll;

    @ManyToOne
    @JoinColumn(name = "id_course", nullable = false, foreignKey = @ForeignKey(name = "fk_enroll_detail_course"))
    private Course course;
}
