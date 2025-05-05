Domain object
===============
---

Status
======
---
Created

Context
======
---
Create domain model with JPA needed specifications and Builder pattern to use, as well as all the needed methods: getters, setters etc.

Decision
=====
---
Simple @Entity created at the moment with hand build methods, no dependencies added.

Consequences
===
---
JPA needs default constructor and all the usual methods. If those are not included, object will not be returned.