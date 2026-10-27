# CRUD Workflow

Build the CRUD stack for one or many entities, then review it before reporting back.

## Assigned Agents

Run them in this order — the review only makes sense once the code exists.

| Step | Agent | Model | Effort |
|------|-------|-------|--------|
| 1. Build | `crud-builder` | `sonnet` | `high` |
| 2. Review | `code-reviewer` | `opus` | `high` |

Model and effort are declared in each agent's own frontmatter; do not override them from here.

## Required Input

* Entity names in singular UpperCamelCase — one, several, or "all remaining".
* Fields with types, lengths, required flags, and relationships, for entities that do not exist yet under `model/`.
* DTO field names when they differ from the entity fields.
* API path under `/v1` when it is not the plural lowercase form of the entity name.

## Workflow

1. Validate the input. For entities that already exist in `model/`, let `crud-builder` derive fields and keys by reading them instead of asking the user.
2. Delegate the implementation to `crud-builder`. It generates model, DTO, repo, service interface, service impl, controller, and mapper configuration per entity, following the `Student` vertical.
3. Confirm `crud-builder` reported a successful `./mvnw -DskipTests compile`. If it did not, send it back to fix the errors before continuing — never hand broken code to the reviewer.
4. Pass the list of created and modified files to `code-reviewer`.
5. Apply the findings ranked high severity and above. Route each fix back to `crud-builder`, then re-run step 3.
6. Repeat steps 4–5 until the review returns no high-severity findings.
7. Return a final summary:

    * Changed files, grouped by entity
    * Endpoint contract per entity
    * Validation rules
    * Mapper strategy used
    * Review findings: fixed, and accepted as-is with the reason
    * Compilation and test results

## Acceptance Criteria

* Each entity follows the existing CRUD architecture: `IGenericRepo`, `ICRUD`, `CRUDImpl`.
* DTO validation rejects invalid request payloads.
* Create returns `201 Created`; reads and update return `200 OK`; delete returns `204 No Content`.
* Not-found flows reach `GlobalErrorHandler` through `ModelNotFoundException`.
* Entity/DTO field mismatches are mapped in both directions.
* The project compiles and the existing test suite passes.
* No shared CRUD or exception abstraction is modified unless explicitly required.
* No entity in the request is left unbuilt without a stated reason.
