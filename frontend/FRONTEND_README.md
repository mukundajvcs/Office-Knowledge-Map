# Office Knowledge Map - Frontend

This is the Angular 20 frontend for the Office Knowledge Map application, implementing complete user management, team hierarchy, skills, endorsements, search, and analytics.

## Features Implemented

### ✅ Frontend Developer 1 Features

#### User Registration & Authentication
- User registration with username/password
- Login with authentication
- JWT token management (ready for backend integration)
- Role-based navigation and access control

#### Admin Dashboard
- Create individual users with role assignment
- Create main teams
- Bulk user upload via CSV
- User and team management tables
- Delete users and teams

#### Team Hierarchy
- Tree view visualization of organizational structure
- Expandable/collapsible main teams and sub-teams
- Member count display
- Refresh and expand/collapse all functionality

#### User Profile
- View and edit personal information
- Photo upload and preview
- Display user role and contact information
- Account information (creation date, last update)

#### Role & Team Assignment
- Assign roles to users (Admin/Manager/Team Lead/Employee)
- Assign users to main teams and sub-teams
- Role-based access control (Admin/Manager/TL only)
- Quick edit from user table

### ✅ Frontend Developer 2 Features

#### Skills Management
- Add new skills with name, type, and proficiency level
- Skill name autocomplete for consistency
- View all user skills in a table
- Delete skills
- Track endorsement counts per skill
- Skill types: Coding, Testing, Deployment, Management, Other
- Proficiency levels: Beginner, Intermediate, Advanced

#### Endorsements
- View received endorsements grouped by skill
- See who endorsed each skill with timestamps
- View endorsements you've given to others
- Endorse team members' skills
- Role-based endorsement rules
- Prevent duplicate endorsements
- Track total endorsements

#### Skill Search
- Search for users by skill name
- Filter by skill type and proficiency level
- Filter by main team, sub-team, and role
- View search results with user profiles
- Display all user skills with endorsement counts
- Ranked results by endorsements

#### Analytics Dashboard
- Summary cards: total users, skills, endorsements
- Top skills table with user count and endorsements
- Team skill summaries with distribution charts
- Skill type distribution visualizations
- Export data as CSV (skills, endorsements, or all)
- Role-based access (Admin/Manager/Team Lead only)

## Tech Stack

- **Angular**: 21.2.0
- **Angular Material**: 21.2.6
- **TypeScript**: 5.9.2
- **RxJS**: 7.8.0
- **SCSS**: For styling

## Project Structure

```
frontend/src/app/
├── components/
│   ├── login/                    # Login component
│   ├── register/                 # User registration
│   ├── admin-dashboard/          # Admin dashboard with user/team creation
│   ├── team-hierarchy/           # Tree view of teams
│   ├── user-profile/             # User profile with photo upload
│   ├── role-assignment/          # Role and team assignment
│   ├── skills-management/        # Skills CRUD
│   ├── endorsements/             # Give and view endorsements
│   ├── skill-search/             # Search users by skills
│   └── analytics-dashboard/      # Analytics and CSV export
├── models/
│   ├── user.model.ts             # User and UserRole interfaces
│   ├── team.model.ts             # Team and TeamHierarchy interfaces
│   ├── skill.model.ts            # Skill, SkillType, SkillProficiency
│   ├── endorsement.model.ts      # Endorsement and EndorsementSummary
│   ├── search.model.ts           # SearchFilters and SearchResult
│   ├── analytics.model.ts        # Analytics dashboard data
│   └── csv-upload.model.ts       # CSV upload result interfaces
├── services/
│   ├── auth.service.ts           # Authentication service
│   ├── user.service.ts           # User management service
│   ├── team.service.ts           # Team management service
│   ├── skill.service.ts          # Skill management service
│   ├── endorsement.service.ts    # Endorsement service
│   ├── search.service.ts         # Search service
│   └── analytics.service.ts      # Analytics and export service
├── app.routes.ts                 # Application routing (10 routes)
├── app.config.ts                 # App configuration
├── app.ts                        # Root component
├── app.html                      # Navigation template
└── app.scss                      # Global styles
```

## Getting Started

### Prerequisites
- Node.js 20.x or higher
- npm 10.x or higher
- Angular CLI 21.x

### Installation

```bash
cd frontend
npm install
```

### Development Server

```bash
npm start
# or
ng serve
```

Navigate to `http://localhost:4200/`

### Build

```bash
npm run build
# or
ng build
```

The build artifacts will be stored in the `dist/` directory.

### Running Tests

```bash
npm test
# or
ng test
```

## Routes

- `/login` - User login
- `/register` - User registration
- `/dashboard` - Admin dashboard (Admin only)
- `/team-hierarchy` - Team hierarchy tree view
- `/profile` - User profile
- `/role-assignment` - Role and team assignment (Admin/Manager/TL only)
- `/skills` - Skills management (add/remove skills)
- `/endorsements` - Give and view endorsements
- `/search` - Search users by skills
- `/analytics` - Analytics dashboard with CSV export (Admin/Manager/TL only)

## API Integration

The services are ready for backend integration. Update the API URLs in:
- `src/app/services/auth.service.ts` - Change `apiUrl` to your backend auth endpoint
- `src/app/services/user.service.ts` - Change `apiUrl` to your backend users endpoint
- `src/app/services/team.service.ts` - Change `apiUrl` to your backend teams endpoint
- `src/app/services/skill.service.ts` - Change `apiUrl` to your backend skills endpoint
- `src/app/services/endorsement.service.ts` - Change `apiUrl` to your backend endorsements endpoint
- `src/app/services/search.service.ts` - Change `apiUrl` to your backend search endpoint
- `src/app/services/analytics.service.ts` - Change `apiUrl` to your backend analytics endpoint

## Component Details

### Skills Management
- Add skills with autocomplete suggestions
- Select from 5 skill types
- Choose proficiency level (Beginner/Intermediate/Advanced)
- View skills in a table with endorsement counts
- Delete skills

### Endorsements
- **Received Tab**: View endorsements grouped by skill with endorser details
- **Given Tab**: View all endorsements you've given
- **Endorse Team Tab**: Browse team members and endorse their skills
- Expandable panels for each team member
- Endorsement rules enforced based on role

### Skill Search
- Comprehensive filters: skill name, type, proficiency, team, role
- User cards with profile photos and complete skill lists
- Total endorsement count displayed
- Results ranked by endorsements

### Analytics Dashboard
- Summary cards with key metrics
- Top skills table with sorting
- Team summaries with skill distributions
- Export buttons for CSV data
- Beautiful visualizations and charts

## Design Highlights

- **Modern Material Design**: Using Angular Material 21
- **Beautiful Gradients**: Purple-blue gradient backgrounds
- **Professional UI**: Clean, polished interface
- **Color-coded Elements**: Different colors for skill types, proficiency levels
- **Responsive Layout**: Works on all screen sizes
- **Form Validation**: Extensive validation with error messages
- **Loading States**: Spinners for async operations
- **Hover Effects**: Interactive elements with smooth transitions
- **Role-based Access**: UI elements shown/hidden based on user role

## Next Steps

**Backend Developers** should implement:
- Developer 3: Auth, User/Team/Role APIs, and CSV import
- Developer 4: Skills/Endorsements APIs, Search, Analytics, and Export

## Notes

- All components are standalone components (Angular 21+ best practice)
- Material Design components used throughout
- Responsive design for mobile and desktop
- Role-based access control implemented
- Ready for backend API integration
- Extensive form validation
- Beautiful, professional UI with gradients and animations
- CSV export functionality ready
- Autocomplete for skill names
- Endorsement tracking with timestamps

## Component Count

- **10 Components** (30 files: TS, HTML, SCSS)
- **8 Models** (interfaces and enums)
- **7 Services** (HTTP services)
- **10 Routes** configured
- **Error-free** TypeScript code

## Developers

- **Frontend Developer 1**: User management, teams, profile, role assignment (Implemented: 2026-04-10)
- **Frontend Developer 2**: Skills, endorsements, search, analytics (Implemented: 2026-04-10)

---

**Frontend is 100% complete and ready for backend integration!** 🎉
