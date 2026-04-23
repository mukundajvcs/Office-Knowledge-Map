# ROADMAP.md

## Office Knowledge Map – Development Roadmap

_Last updated: 2026-04-10_

---

### Legend
- [ ] Not started
- [~] In progress
- [x] Complete

---

## Team Assignment
- **Developer 1:** Frontend – User Management, Team Hierarchy, Profiles
- **Developer 2:** Frontend – Skills, Endorsements, Search, Analytics
- **Developer 3:** Backend – Auth, User/Team/Role APIs, CSV Import
- **Developer 4:** Backend – Skills/Endorsements APIs, Search, Analytics, Export

---

## 1. Frontend Tasks

### Developer 1
#### User Management & Team Hierarchy
- [x] User registration (username/password)
- [x] Admin dashboard: create main teams, users (single & CSV)
- [x] Team hierarchy visualization (tree view)
- [x] User profile UI (name, role, contact info, photo upload)
- [x] Role/team assignment UI (Admin/Manager/TL only)

### Developer 2
#### Skills, Endorsements, Search, Analytics
- [x] Skills management UI (add/remove, type, proficiency)
- [x] Endorsement UI (endorse, view endorsements)
- [x] Search UI (by skill, team, role, level; ranking)
- [x] Analytics dashboard (top skills, endorsement trends)
- [x] CSV export UI

---

## 2. Backend Tasks

### Developer 3
#### Auth, User/Team/Role APIs, CSV Import
- [x] User authentication (registration, login)
- [x] Admin user bootstrap
- [x] User CRUD APIs
- [x] Team & sub-team CRUD APIs
- [x] Role assignment logic
- [x] Bulk user creation via CSV upload

### Developer 4
#### Skills/Endorsements APIs, Search, Analytics, Export
- [x] Skills CRUD APIs (custom skills, types, proficiency)
- [x] Endorsements APIs (rules, tracking, read-only)
- [x] Search API (filters, ranking)
- [x] Analytics API (top skills, trends)
- [x] CSV export API

---

## 3. Integration & QA
- [x] Frontend-backend integration (all devs)
- [ ] End-to-end testing (all devs)
- [ ] Documentation & logging (all devs)

---

## 4. Deployment
- [ ] Azure PaaS deployment scripts
- [ ] Release checklist

---

_Developers: Update this file with [~] or [x] as you progress. Do not overwrite sections assigned to other developers._
