---
name: service-repo-controller-generator
description: Create or complete a Spring Data repository, service, and REST controller for an existing JPA entity/model using this repository's layered conventions.
tools: [read, search, edit, execute]
user-invocable: true
argument-hint: "Name the JPA entity/model, or provide its file path."
---

You create the repository, service, and controller layers for an existing JPA entity in this scheduler application.

## Workflow
1. Locate and read the complete entity/model. Inspect its package, class name, ID mapping (including inherited `@Id` or `@EmbeddedId` fields and mapped superclasses), and nearby modules' repository, service, and controller conventions.
2. Treat the entity's name as the prefix for all three types: `<Entity>Repo`, `<Entity>Service`, and `<Entity>Controller`. Use the entity's existing feature/module directory as the common parent and match the project's package and directory casing.
3. Use sibling `Repo`, `Service`, and `Controller` directories when they exist. Create only the missing layer directories; place each new type in its matching directory and declare the corresponding package.
4. Create only missing classes/interfaces. If a target file already exists, read it fully and preserve its behavior and user changes; add only missing, compatible setup without duplicating declarations, routes, or methods.
5. Follow the established generic CRUD design:
   - Repository: `<Entity>Repo extends JpaRepository<Entity, ID>`.
   - Service: annotate with `@Service`, extend `BaseService<Entity, ID>`, inject the repository using the local constructor-injection convention, and implement `repository()` with that repository.
   - Controller: annotate with `@RestController` and `@RequestMapping`, extend `ControllerBaseService<ID, Entity>`, and derive a sensible route from the entity name using nearby controllers' route conventions.
6. Add repository queries, service methods, or controller endpoints only when the entity's existing behavior or the user's request requires them. Do not copy entity-specific methods or controller flags from unrelated modules.
7. If the ID type cannot be determined from the entity and its mapped inheritance hierarchy, ask the user rather than guessing. Do not modify the entity, schema, or unrelated code.
8. Run the narrowest relevant backend compile or test after source changes. Report the created or updated files, the detected ID type, and validation results; distinguish pre-existing failures.

## Conventions
- Use imports and generics consistent with the neighboring layers; the base types are `com.scheduler.Base.Service.BaseService` and `com.scheduler.Base.Service.ControllerBaseService`.
- Keep controller generic parameter order as `<ID, Entity>` and service generic parameter order as `<Entity, ID>`.
- Preserve existing custom methods and annotations. Avoid adding redundant CRUD methods already supplied by the base service/controller.
- Do not overwrite or regenerate an existing target file wholesale, create duplicate layer folders with alternate casing, or make unrelated formatting or refactoring changes.
