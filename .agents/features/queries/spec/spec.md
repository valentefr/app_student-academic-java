# Courses With Students JPQL Query

# Part 1 — Specification

## Problem

The project only exposes generic CRUD per entity. There is no way to get, in a single response, which students are enrolled in each course, or to filter that view by course status or by school year.

## Goal

Add a read-only JPQL query, exposed as a `GET` endpoint, that returns **one element per course**, each with the list of its enrolled students. The course-status and school-year filters are resolved in the database.

## Scope

**In scope**

* A JPQL query with `@Query` in `IEnrollDetailRepo`, returning flat rows.
* Grouping of those rows by course in `EnrollDetailServiceImpl`.
* A `GET` endpoint in `EnrollDetailController`.
* Three DTOs: `CourseStudentDTO` (internal flat row), `StudentSummaryDTO`, and `CourseWithStudentsDTO` (response).

**Out of scope**

* Client-configurable pagination and sorting.
* Security and authorization.
* Changes to `ICRUD`, `CRUDImpl`, `IGenericRepo`, or `GlobalErrorHandler`.
* Schema or entity changes.
* Courses with no enrollments (they do not appear in the result).

## Decisions

* JPQL does not build nested lists, so the query returns flat rows (one per course/student pair) and the service groups them by `idCourse`. Grouping is by id rather than by `name`, because `name` is not unique.
* `CourseStudentDTO` is an internal `SELECT new` projection; it is not exposed to the client.
* The `enabled` and `year` filters are optional. Without them, the query returns everything.
* `DISTINCT` avoids repeating a student if they have multiple enrollments in the same course within the same year.
* Ordering is by `c.name`, `c.idCourse`, `s.lastName`, `s.firstName`. The order of courses in the response follows that order.
* With no results, `200` is returned with an empty list.
* Key convention: `EnrollDetail`'s composite PK is `idEnrollDetail` (`EnrollDetailPK`) with `schoolTerm`, `enroll`, and `course`.

## Data Flow

```
HTTP GET ──> EnrollDetailController ──> EnrollDetailServiceImpl ──> IEnrollDetailRepo @Query (JPQL) ──> PostgreSQL
                                             │                              │
                                             │   flat CourseStudentDTO rows <┘
                                             └── groups by idCourse ──> List<CourseWithStudentsDTO>
```

## Contract

| Method | Path | Parameters | Response |
|--------|------|------------|-----------|
| GET | `/v1/enrolldetails/course-students` | `enabled` (query, Boolean, optional), `year` (query, Integer, optional) | `200` list of `CourseWithStudentsDTO` |

Shape of each element:

```json
{
  "idCourse": 1,
  "acronym": "MAT",
  "name": "Matemática",
  "students": [
    { "dni": "12345678", "firstName": "Ana", "lastName": "Pérez" },
    { "dni": "87654321", "firstName": "Luis", "lastName": "Gómez" }
  ]
}
```

* `acronym` is optional on `Course` and is omitted via `NON_NULL`.
* A non-numeric value for `year` or a non-boolean value for `enabled` produces `400` from Spring's conversion.
* Errors follow the `CustomErrorResponse` shape via `GlobalErrorHandler`.

## Query

```
SELECT DISTINCT new com.vflores.dto.queries.CourseStudentDTO(c.idCourse, c.acronym, c.name, s.DNI, s.firstName, s.lastName)
FROM EnrollDetail ed
JOIN ed.idEnrollDetail.course c
JOIN ed.idEnrollDetail.enroll e
JOIN e.student s
JOIN ed.idEnrollDetail.schoolTerm st
WHERE (:enabled IS NULL OR c.enabled = :enabled)
  AND (:year IS NULL OR st.year = :year)
ORDER BY c.name, c.idCourse, s.lastName, s.firstName
```

## Expected Files

**New**

* `dto/CourseStudentDTO.java` — flat row: `idCourse`, `acronym`, `name`, `dni`, `firstName`, `lastName`.
* `dto/StudentSummaryDTO.java` — `dni`, `firstName`, `lastName`.
* `dto/CourseWithStudentsDTO.java` — `idCourse`, `acronym`, `name`, `students`.

**Modified**

* `repo/IEnrollDetailRepo.java` — `findCourseStudents` method.
* `service/IEnrollDetailService.java`, `service/impl/EnrollDetailServiceImpl.java` — `findCoursesWithStudents` method with the grouping.
* `controller/EnrollDetailController.java` — `GET /course-students` endpoint.

## Acceptance Criteria

* The project compiles with `./mvnw.cmd -q compile`.
* With no filters, the response has one element per course with enrollments, and each lists all of its students.
* With `enabled=true` only enabled courses appear; with `enabled=false`, only disabled ones.
* With `year=<year>` only enrollments from school terms of that year appear.
* A student with two enrollments in the same course in the same year appears only once in that course's list.
* A filter with no matching data returns `[]` with `200`.
* `ICRUD`, `CRUDImpl`, `IGenericRepo`, and `GlobalErrorHandler` do not change.

## Prerequisites

* PostgreSQL available at `localhost:5432/db_students_frv` (or `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`).
* `Student`, `Course`, `SchoolTerm`, `Enroll`, and `EnrollDetail` data created via `POST`. `POST` returns the DTO with the generated id.

---

# Part 2 — Implementation

## Files

### New

* `dto/CourseStudentDTO.java`
* `dto/StudentSummaryDTO.java`
* `dto/CourseWithStudentsDTO.java`

### Modified

* `repo/IEnrollDetailRepo.java` — `findCourseStudents(Boolean enabled, Integer year)` with `@Query`.
* `service/IEnrollDetailService.java` — declaration of `findCoursesWithStudents`.
* `service/impl/EnrollDetailServiceImpl.java` — grouping by `idCourse` with `LinkedHashMap`.
* `controller/EnrollDetailController.java` — `GET /course-students` with `@RequestParam(required = false)`.

## Deviations from the spec

None.

## Verification

Compilation (from the project root):

```
./mvnw.cmd -q compile
```

Status: compiles with no errors.

Runtime verification (app running via `./mvnw.cmd spring-boot:run` against local PostgreSQL, data seeded via `POST`: enabled course "Matematica" and disabled course "Historia", school terms 2025-1 and 2026-1, two students, cross enrollments):

* No filters → one element per course with enrollments; "Matematica" lists both students, "Historia" only its own. OK.
* `enabled=true` → "Historia" (disabled) does not appear. OK.
* `enabled=false` → only "Historia" appears. OK.
* `year=2025` → "Matematica" only lists the student enrolled that year; "Historia" appears the same. OK.
* `year=2026` → only "Matematica" with the student from that term. OK.
* `year=2025&enabled=true` → correctly combines both filters. OK.
* `year=1900` (no data) → `200` with `[]`. OK.
* No student appears duplicated within the same course.

The risk flagged about `(:enabled IS NULL OR ...)` with a `null` parameter did not materialize: Hibernate/PostgreSQL resolved the type without needing a `CAST`.
