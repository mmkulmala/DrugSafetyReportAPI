Spring Boot application
===============
---

Status
======
---
Created

Context
======
---
Create Spring Boot application set by Tepsivo assignment.

Decision
=====
---
Choosed Java and JVM 21 as it is LTS (Long Term Support), which is always a safe choice. Also didn't use Records as they
will extra wrapping as Hibernate won't support them yet, so keeping thing easy to understand.

Consequences
===
---
Kotlin might have been a good choice, but Java is still the best way to Spring Boot as it most used JVM language