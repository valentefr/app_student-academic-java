package com.vflores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Enroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_enrolled")
    private Integer idEnroll;

    @ManyToOne
    @JoinColumn(name = "id_student", nullable = false, foreignKey = @ForeignKey(name = "fk_enroll_student"))
    private Student student;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean enabled;

//    @OneToMany(mappedBy = "idEnrollDetail.enroll", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    private List<EnrollDetail> details;
}
