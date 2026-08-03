# AGENTS.md

Early-stage Spring Boot helpdesk API (Maven, Java 21). No README, CI, or lint/format config exist yet.

## Commands
- `mvn` is NOT on PATH. Use the wrapper on Windows: `.\mvnw.cmd test`, `.\mvnw.cmd package` (Maven 3.9.16, Java 21).
- Build artifacts land in `target/` (gitignored).

## Stack quirks (Spring Boot 4.1.0)
- Boot 4 changed starter artifact names. This project uses `spring-boot-starter-webmvc` (NOT `-web`) and test starters suffixed `-test` (e.g. `spring-boot-starter-webmvc-test`). Match this naming when adding dependencies in `pom.xml`.
- Lombok requires explicit `annotationProcessorPaths` in the `maven-compiler-plugin` config (already wired); new code can rely on Lombok annotations.

## Database
- MySQL `helpdesk_db` at `localhost:3306`, credentials hardcoded in `src/main/resources/application.properties` (`root` / `1390`).
- `spring.jpa.hibernate.ddl-auto=create-drop` drops and recreates the schema on every startup — do not assume persistent data.
- The context-loading test (`@SpringBootTest`) boots the full context, so a running MySQL instance is required to run tests.

## Conventions
- Package root: `com.jose_santamaria.helpdesk_api`.
- Subpackages are lowercase: `models`, `enums`, `repositorys` (note the intentional misspelling of "Repositorys" — preserve it), `docs`. `enums` is plural because `enum` is a reserved Java keyword and cannot be a package name.
- No Controllers/Services/DTOs yet.
- Do not commit credentials; DB settings currently live inline in `application.properties` rather than env vars.
