# Entity-Relationship Diagram

![Entity-Relationship Diagram](entity-relationship-diagram.png)

```mermaid
erDiagram
    STUDENT {
        Integer idStudent PK
        String firstName
        String lastName
        String DNI
        LocalDate birthDate
        String email
    }
    COURSE {
        Integer idCourse PK
        String name
        String acronym
        boolean enabled
    }
    SCHOOL_TERM {
        Integer idSchoolTerm PK
        Integer year
        Integer semester
    }
    ENROLL {
        Integer idEnroll PK
        Integer idStudent FK
        LocalDate date
        boolean enabled
    }
    ENROLL_DETAIL {
        Integer idSchoolTerm PK_FK
        Integer idEnroll PK_FK
        Integer idCourse PK_FK
        String classRoom
    }

    STUDENT ||--o{ ENROLL : "enrolls in"
    ENROLL ||--o{ ENROLL_DETAIL : "itemized by"
    COURSE ||--o{ ENROLL_DETAIL : "taken via"
    SCHOOL_TERM ||--o{ ENROLL_DETAIL : "taught in"
```

## Notes

* `EnrollDetail` has no own identity column; its `@EmbeddedId` (`EnrollDetailPK`) is the composite of `schoolTerm` + `enroll` + `course`, so it is the junction entity resolving the `Enroll` × `Course` × `SchoolTerm` relationship.
* `SchoolTerm` enforces a unique `(year, semester)` pair.
* `Enroll.idEnroll` is mapped to column `id_enrolled`.
* `Student` ↔ `Enroll` is the only direct many-to-one in the model; `Course` and `SchoolTerm` only relate to data through `EnrollDetail`.
