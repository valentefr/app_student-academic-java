# Repo & Service Layer UML Class Diagram

![Repo & Service UML Class Diagram](repo-service-uml-diagram.png)

```mermaid
classDiagram
    direction TB

    %% ---- repo package ----
    class IGenericRepo~T, ID~ {
        <<interface>>
    }
    class IStudentRepo {
        <<interface>>
    }
    class ICourseRepo {
        <<interface>>
    }
    class ISchoolTermRepo {
        <<interface>>
    }
    class IEnrollRepo {
        <<interface>>
    }
    class IEnrollDetailRepo {
        <<interface>>
        +findCourseStudents(Boolean enabled, Integer year) List~CourseStudentDTO~
    }

    IGenericRepo <|-- IStudentRepo
    IGenericRepo <|-- ICourseRepo
    IGenericRepo <|-- ISchoolTermRepo
    IGenericRepo <|-- IEnrollRepo
    IGenericRepo <|-- IEnrollDetailRepo

    %% ---- service package ----
    class ICRUD~T, ID~ {
        <<interface>>
        +save(T t) T
        +update(ID id, T t) T
        +findall() List~T~
        +findById(ID id) T
        +delete(ID id) void
    }

    class CRUDImpl~T, ID~ {
        <<abstract>>
        #getRepo()* IGenericRepo~T, ID~
        +save(T t) T
        +update(ID id, T t) T
        +findall() List~T~
        +findById(ID id) T
        +delete(ID id) void
    }

    class IStudentService {
        <<interface>>
    }
    class ICourseService {
        <<interface>>
    }
    class ISchoolTermService {
        <<interface>>
    }
    class IEnrollService {
        <<interface>>
    }
    class IEnrollDetailService {
        <<interface>>
        +findCoursesWithStudents(Boolean enabled, Integer year) List~CourseWithStudentsDTO~
    }

    class StudentServiceImpl {
        -IStudentRepo repo
        #getRepo() IGenericRepo~Student, Integer~
    }
    class CourseServiceImpl {
        -ICourseRepo repo
        #getRepo() IGenericRepo~Course, Integer~
    }
    class SchoolTermServiceImpl {
        -ISchoolTermRepo repo
        #getRepo() IGenericRepo~SchoolTerm, Integer~
    }
    class EnrollServiceImpl {
        -IEnrollRepo repo
        #getRepo() IGenericRepo~Enroll, Integer~
    }
    class EnrollDetailServiceImpl {
        -IEnrollDetailRepo repo
        #getRepo() IGenericRepo~EnrollDetail, EnrollDetailPK~
        +findCoursesWithStudents(Boolean enabled, Integer year) List~CourseWithStudentsDTO~
    }

    ICRUD <|.. CRUDImpl
    ICRUD <|-- IStudentService
    ICRUD <|-- ICourseService
    ICRUD <|-- ISchoolTermService
    ICRUD <|-- IEnrollService
    ICRUD <|-- IEnrollDetailService

    CRUDImpl <|-- StudentServiceImpl
    CRUDImpl <|-- CourseServiceImpl
    CRUDImpl <|-- SchoolTermServiceImpl
    CRUDImpl <|-- EnrollServiceImpl
    CRUDImpl <|-- EnrollDetailServiceImpl

    IStudentService <|.. StudentServiceImpl
    ICourseService <|.. CourseServiceImpl
    ISchoolTermService <|.. SchoolTermServiceImpl
    IEnrollService <|.. EnrollServiceImpl
    IEnrollDetailService <|.. EnrollDetailServiceImpl

    StudentServiceImpl ..> IStudentRepo : uses
    CourseServiceImpl ..> ICourseRepo : uses
    SchoolTermServiceImpl ..> ISchoolTermRepo : uses
    EnrollServiceImpl ..> IEnrollRepo : uses
    EnrollDetailServiceImpl ..> IEnrollDetailRepo : uses
```

## Notes

* `IGenericRepo<T, ID>` is a `@NoRepositoryBean` base over Spring Data's `JpaRepository<T, ID>`; every per-entity repo (`IStudentRepo`, `ICourseRepo`, `ISchoolTermRepo`, `IEnrollRepo`, `IEnrollDetailRepo`) only extends it — no custom methods except `IEnrollDetailRepo.findCourseStudents`.
* `ICRUD<T, ID>` + `CRUDImpl<T, ID>` hold all generic CRUD logic (`save`, `update`, `findall`, `findById`, `delete`). Concrete services implement a marker interface (`IStudentService extends ICRUD<...>`, etc.) and extend `CRUDImpl`, supplying their repo via the abstract `getRepo()` hook.
* `IEnrollDetailService`/`EnrollDetailServiceImpl` is the one vertical that adds real logic on top of the generic base: `findCoursesWithStudents` groups the flat rows from `IEnrollDetailRepo.findCourseStudents` by course.
* `CRUDImpl.update` relies on the `set + EntityName` reflective naming convention described in `CLAUDE.md`; it is not visible in this class-level diagram.
