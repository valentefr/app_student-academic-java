# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1.1 / Java 25 REST API for managing students (`com.vflores`, artifact `app-students`). Maven wrapper is committed; there is no README beyond the generated `HELP.md`.

## Commands

Use `./mvnw` (Git Bash) or `.\mvnw.cmd` (PowerShell).

```bash
./mvnw spring-boot:run          # run the app (needs PostgreSQL, see below)
./mvnw clean package            # build jar into target/
./mvnw test                     # all tests
./mvnw test -Dtest=ClassName#methodName   # single test / method
```

There is no linter or formatter configured.

## Database

PostgreSQL is required at runtime; schema is managed by Hibernate `ddl-auto: update` (no migration tool). Defaults in `src/main/resources/application.yaml` point at `localhost:5432/db_students_frv`, overridable via `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.

## Architecture

Layered generic-CRUD design — each entity is expected to be wired through four parallel generic types:

- `repo/IGenericRepo<T, ID>` — `@NoRepositoryBean` base over `JpaRepository`; per-entity repos (`IStudentRepo`) only extend it.
- `service/ICRUD<T, ID>` + `service/impl/CRUDImpl<T, ID>` — abstract base holding all CRUD logic; concrete services (`StudentServiceImpl`) extend it, implement a marker interface (`IStudentService extends ICRUD<...>`), and supply the repo by overriding `getRepo()`.
- `controller/StudentController` — maps `/v1/students`, converts entity ↔ DTO through the injected `ModelMapper` bean (`config/MapperConfig`) in private `toDTO`/`toEntity` helpers. Controllers never expose entities. `POST` returns **201 with the created DTO as the body** (generated id included) and no `Location` header; `PUT` returns 200 with the updated DTO, `DELETE` returns 204.
- `dto/StudentDTO` — jakarta validation (`@NotNull`) lives on the DTO only; entities carry only JPA column constraints.

Adding an entity means following that same five-file pattern (model, DTO, repo, service interface + impl, controller), not adding logic to the generic base. All five entities (`Student`, `Course`, `SchoolTerm`, `Enroll`, `EnrollDetail`) are complete verticals — copy the closest one rather than inventing a variation.

### Conventions that the generic code depends on

- **Primary key naming**: `CRUDImpl.update` resolves the setter reflectively as `"setId" + entitySimpleName` (e.g. `setIdStudent`), looked up with `id.getClass()` as the parameter type. An entity's `@Id`/`@EmbeddedId` field **must** therefore be named `id<EntityName>` or `update` fails at runtime. Every entity now satisfies this: `Enroll`'s field is `idEnroll` (mapped to column `id_enrolled`) and `EnrollDetail`'s `@EmbeddedId` is `idEnrollDetail` of type `EnrollDetailPK`.
- `ICRUD.findall()` is lowercase-`a` — intentional in the current code, keep call sites consistent.
- Entities use Lombok `@Data @AllArgsConstructor @NoArgsConstructor` plus `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` with `@EqualsAndHashCode.Include` on the id only. Lombok is wired as an annotation processor explicitly in `pom.xml`.
- `ModelNotFoundException` (in the root `com.vflores` package) is thrown from `CRUDImpl.findById`/`delete`.

### Error handling

`config/GlobalErrorHandler` is a `@RestControllerAdvice extends ResponseEntityExceptionHandler`. All responses use the `CustomErrorResponse` record (timestamp, message, path). Mapping: `ModelNotFoundException` → 404, `ArithmeticException`/`NumberFormatException` → 406, validation failures via the overridden `handleMethodArgumentNotValid` → 400, everything else → 500. Add new mappings here rather than catching in controllers.

### Implemented resources

All five are full verticals, and all five have been exercised against a running app and a live PostgreSQL database.

- `Student` — `/v1/students`. **`StudentDTO.DNI` serializes as `"DNI"`, not `"dni"`** — Lombok generates `getDNI()`/`setDNI()` and Jackson keeps the all-caps name, so a payload using `"dni"` silently drops the value and fails `@NotNull` with a 400. The same caveat applies to any future all-caps DTO field.
- `Course` — `/v1/courses`. `CourseDTO.enabled` is a `Boolean` (so `@NotNull` is meaningful) against the entity's primitive `boolean`; ModelMapper converts that by default.
- `SchoolTerm` — `/v1/schoolterms`. `year` and `semester` are range-validated on the DTO (`@Min`/`@Max`; semester is 1 or 2).
- `Enroll` — `/v1/enrolls`. `EnrollDTO.student` is `@NotNull @Valid`, so the nested student is **fully** validated: an id-only `{"idStudent": 1}` is rejected with a 400 and every `@NotNull` leaf field must be present. Nothing cascades to the parent row, so placeholder values on those leaves are echoed back in the response but do not overwrite the real `Student`.
- `EnrollDetail` — `/v1/enrolldetails`, keyed by the `EnrollDetailPK` `@EmbeddedId` (`schoolTerm`, `enroll`, `course`). Single-`{id}` paths do not apply: `GET`/`PUT`/`DELETE` take `/{idSchoolTerm}/{idEnroll}/{idCourse}` and the controller rebuilds the PK in a private `toPK` helper. `EnrollDetailPKDTO`'s three fields are `@NotNull` but not `@Valid`, so unlike `Enroll` above, id-only nested objects are accepted here.

## Vendored skills

`.agents/skills/` contains third-party skills pinned by `skills-lock.json` (`dr-jskill`, `java-code-review`, `java-testing`). It is vendored content — don't edit it as project code.

## Project agents and workflows

Agents live in `.agents/subagents` (`crud-builder`, `code-reviewer`, `data-generator`) and workflows in `.agents/workflows`. Claude Code does **not** load that layout — it reads agents from `.claude/agents/`, so a request to "use the crud-builder agent" means reading the definition file and following it (or copying it into `.claude/agents/` first).

`data-generator.md` is also stale: it names the project `mito-sales`, and its "the fact that drives everything" — that `POST` returns 201 with an empty body and no id, so children need a `GET`-and-match step to resolve parent ids — is wrong here. `POST` returns the created DTO with its generated id, so children can be built straight from the response.

`crud-builder.md` has stale code samples: `ICRUD<Student, Integer>` and `implements IStudentService` on a non-Student impl, `findAll()` instead of `findall()`, a `@Qualifier`-injected mapper, an `exception` package, a `201`-with-empty-body controller, and references to a `StudentMapper` bean and an `IngressDetailPK` class that do not exist here. The live `Student`/`Course` code is the correct reference.