# Jira Clone Backend (Spring Boot)

Minimal REST API backend: Auth, Projects, Issues, Comments.

## Run it

```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`. Uses an in-memory H2 database by default (data resets on restart) — no DB install needed to try it out. To switch to MySQL, edit `application.properties` (instructions are in the file).

H2 console (to inspect tables): `http://localhost:8080/h2-console` — JDBC URL `jdbc:h2:mem:jiraclone`, user `sa`, blank password.

## All routes require a JWT except register/login

Include the header on every other request:
```
Authorization: Bearer <token>
```

See the chat response for full request/response examples and workflow.
