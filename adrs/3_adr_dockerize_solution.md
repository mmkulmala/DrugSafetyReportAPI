Dockerfile
===============
---

Status
======
---
Created

Context
======
---
Create Dockerfile based on best practices. Process is:
1. Use Alpine-based Java images for smaller size
2. Ensure the user creation commands are appropriate for Alpine
3. Include proper health checks with wget
4. Make sure the Maven build process works correctly with the project structure

Adding also compose.yml for docker compose. This file has ports

Decision
=====
---
Chose to follow best practises for Dockerfile to have a good baseline for generating container from API.

Consequences
===
---
Sometimes running applications with user privileges help with security, see [Root user inside a Docker](https://security.stackexchange.com/questions/106860/can-a-root-user-inside-a-docker-lxc-break-the-security-of-the-whole-system). 