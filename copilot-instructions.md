# Copilot Instructions (Concise Edition)

> **Purpose** A single, quick‑reference guide for all contributors. Keep code and docs in sync.

---

## 1. Tech Stack

- **Backend:** Java 17, Maven 3.6.3 Spring Boot 4.0.1, PostgreSQL, Spring Data JPA, Spring Boot starter Web,Lombok
- **Frontend:** Angular 20, Typescript, RxJS, Angular Material

## 2. Repo Layout (trimmed)

project-root/
│
├── frontend/
│   └── src/                # Frontend source code
│
├── backend/
│   └── src/                # Backend source code
│
├── docs/                    # All documentation files (e.g.,instructions, API docs)

## 3. Key Documents

| File            | Purpose                                   |
| --------------- | ----------------------------------------- |
| **PRD.md**      | Functional requirements (source of truth) |
| ROADMAP.md      | Milestones derived from PRD               |
| STATE.md        | Machine‑readable progress tracker         |
| ARCHITECTURE.md | Design & IaC details                      |
| DEPLOYMENT.md   | Release procedures                        |
| TESTING.md      | Test strategy                             |
| API.md          | Swagger / OpenAPI notes                   |
| SECURITY.md     | Security guidelines                       |
| README.md       | Project overview                          |

> **Always update the relevant docs when you change code, tests, or pipelines.**

## 4. Mandatory Workflow

1. **Define requirements** – Update **PRD.md**; get approval.
2. **Plan** – Update **ROADMAP.md** → initialise **STATE.md**.
3. **Design docs** – Update **ARCHITECTURE.md** & other docs _before_ coding.
4. **Implement** – Code & tests; update **ROADMAP.md** & **STATE.md** continuously.
5. **Verify** – Ensure docs match reality.
6. **Deploy** – Follow **DEPLOYMENT.md**; log final state in **STATE.md**.

## 5. STATE.md Schema (summary)

- JSON with: `version`, `lastUpdated`, `projectProgress`, `implementationState` (features, tests, branches), `nextSteps`.
- Update **before & after** each coding session. Use ISO dates (`YYYY‑MM‑DD`).

## 6. Dates & Commits

- Use absolute dates (e.g., `2025‑05‑30`).
- Standard commit flow:

  ```bash
  git add .
  git commit -m "<message>"
  git push
  ```

- Only commit & push whenever the user asks you to. Do not commit on your own.
---

### Remember

- **Azure PaaS only.**
- **Doc‑first.** `PRD.md` & `STATE.md` are authoritative.
- No TODOs in code; update `STATE.md` & `ROADMAP.md` instead.
- Always build in extensive logging for debugging purposes.
---

## 7. Coding Guidelines

### Angular Frontend Guidelines

#### File Structure & Naming
- Use Angular CLI conventions: `kebab-case` for files and folders
- Component files: `component-name.component.ts`, `component-name.component.html`, `component-name.component.scss`
- Service files: `service-name.service.ts`
- Model files: `model-name.model.ts`
- Make UI look beautiful and professional
- Follow Material Design guidelines
