# Product Requirements Document (PRD)

## Product: Office Knowledge Map

### Last Updated: 2026-04-10

---

## 1. Overview

The Office Knowledge Map is a platform for mapping organizational structure, user skills, and endorsements, enabling efficient skill discovery, team management, and analytics within a single organization.

---

## 2. Goals
- Centralize knowledge of team structure and user skills
- Enable skill-based search and discovery
- Facilitate endorsements and analytics for workforce planning

---

## 3. Scope & Priorities

### 3.1 Must-Have (P1)
- Single organization support
- User registration with username/password
- Admin user creation on system setup
- Main team and sub-team hierarchy (strictly two levels)
- User roles: Admin, Manager, Team Lead, Employee
- User profile: name, role, contact info, photo
- Admin can create main teams and users (bulk via CSV)
- Managers/Team Leads can create sub-teams under their main team
- Role assignment within teams and sub-teams
- Users can add/remove their own skills (custom skills allowed)
- Each skill must have a type (coding, testing, deployment, management, other)
- Skill proficiency: Beginner, Intermediate, Advanced
- Endorsements for skills (rules by role)
- Endorsement tracking (who, whom, skill, timestamp)
- Endorsements are read-only (cannot be removed)
- Search for users by skill, team, role, skill level
- Search results ranked by endorsements, then skill level
- Display user skills, levels, and endorsements in search results
- Real-time analytics for Admins/Managers (top skills, endorsement trends)
- Data export in CSV format

### 3.2 Should-Have (P2)
- User profile photo upload and display
- Skill type suggestions/autocomplete
- Endorsement history viewable in user profile
- Real-time report dashboards

### 3.3 Nice-to-Have (P3)
- Skill type taxonomy management by Admin
- Advanced analytics (e.g., skill gap analysis)
- Integration with external HR systems

---

## 4. Functional Requirements

### 4.1 Organization & User Management
- System supports only one organization
- On first setup, an Admin user is created
- User registration via username/password
- Admin can create users (Managers, Team Leads, Employees) individually or via CSV upload
- Admin can create multiple main teams
- Managers/Team Leads can create sub-teams under their main team
- Each sub-team must have assigned roles: Manager, Team Lead, Employees

### 4.2 Team Hierarchy
- Hierarchy: Organization → Main Team → Sub-Team (no deeper nesting)
- Visualize team hierarchy (tree view)
- Users can only belong to one sub-team at a time

### 4.3 User Profiles
- Profile fields: name, role, contact info, photo
- Users can update their own profile (except role/team)
- Admin/Manager/Team Lead can update user roles and team assignments

### 4.4 Skills Management
- Users can add/remove their own skills
- Each skill must have a name, type (coding, testing, deployment, management, other), and proficiency level
- Users cannot modify skills of other users (except endorsements)
- Custom skills allowed, but must select a type

### 4.5 Endorsements
- Users can endorse others for specific skills
- Endorsement rules:
  - Employees: endorse peers in same sub-team
  - Team Leads: endorse anyone in their sub-team
  - Managers: endorse anyone in their sub-team
- System tracks who endorsed whom, for which skill, with timestamp
- Endorsements are read-only (cannot be removed)
- Same person can endorse the same skill multiple times if needed
- No notifications for endorsements

### 4.6 Search & Discovery
- Search for users by skill, with optional filters: main team, sub-team, role, skill level
- Search results ranked by number of endorsements, then skill level
- Display user’s skills, levels, and endorsements in search results
- Search available to Admin, Manager, and Team Lead roles

### 4.7 Analytics & Export
- Admins and Managers can view real-time team skill summaries (top skills per team, endorsement trends)
- Export skill and endorsement data as CSV

---

## 5. Non-Functional Requirements
- Responsive, modern UI (Angular Material)
- Extensive logging for debugging
- Secure authentication and authorization
- Data privacy: only Admin/Manager/Team Lead can view all profiles and analytics
- Azure PaaS deployment only

---

## 6. Out of Scope
- Multi-organization support
- Notifications for endorsements
- Deleting endorsements
- Users belonging to multiple teams/sub-teams

---

## 7. Open Questions
- Should Admins be able to edit or remove endorsements?
- Should there be a review/approval process for bulk user creation?

---

## 8. Appendix
- Skill Types: Coding, Testing, Deployment, Management, Other (extendable)
- Roles: Admin, Manager, Team Lead, Employee

---

_End of Document_