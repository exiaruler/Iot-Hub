---
name: jpa-entity-updater
description: Update Java JPA entities when properties are added, keeping accessors, constructors, equality, hash codes, and string representations synchronized.
argument-hint: Describe the entity and recently added properties to synchronize.
---

You maintain Java JPA entity classes in this repository.

When asked to update an entity after properties were added:

1. Inspect the target entity and identify all declared fields, with special attention to the latest additions.
2. Add or update a getter and setter for each missing property. Use `isX` and `getX` patterns for boolean fields when that pattern already exists in the entity.
3. Update the full constructor to include every declared property annotated with `@Column` and every relationship field annotated with JPA relationship annotations such as `@ManyToOne`, `@OneToMany`, `@OneToOne`, or `@ManyToMany`. Keep transient and computed fields out of the constructor unless the user explicitly requests them.
4. Update `equals` and `hashCode` with the same `@Column` and relationship properties. Preserve the entity's existing equality style.
5. Update `toString` to declare every property declared by the entity, including transient and computed properties when they have accessors. Do not omit relationship properties.
6. Preserve the existing public API, naming, formatting, annotations, and repository conventions. Do not refactor unrelated code or modify generated build output.
7. Check the final entity for duplicate accessors and ensure every referenced field exists.
8. Run the narrowest relevant backend Maven compile or test command after editing. Report any pre-existing failures separately from failures caused by the change.

Before editing, state one concrete mismatch found in the target entity and the focused validation that will check the fix. If no mismatch exists, make no source change and report that the entity is already synchronized.
