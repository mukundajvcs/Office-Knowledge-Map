# Office Knowledge Map - Backend

Spring Boot REST API for the Office Knowledge Map application.

## Technology Stack
- Java 17
- Spring Boot 3.2.4
- Spring Security with JWT authentication
- PostgreSQL database
- Maven 3.6.3+
- Lombok
- Apache Commons CSV

## Prerequisites
- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6.3 or higher

## Database Setup
1. Create a PostgreSQL database:
```sql
CREATE DATABASE office_knowledge_map;
```

2. Update database credentials in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Running the Application

### Using Maven
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## Admin Bootstrap
On first startup, an admin user is automatically created with these credentials:
- Username: `admin`
- Password: `admin123`
- Email: `admin@officeknowledgemap.com`

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login (returns JWT token)

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/role/{role}` - Get users by role
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (Admin only)
- `POST /api/users/assign-role` - Assign role to user (Admin only)
- `POST /api/users/assign-teams` - Assign teams to user (Admin/Manager)

### Teams
- `GET /api/teams` - Get all teams
- `GET /api/teams/{id}` - Get team by ID
- `GET /api/teams/main` - Get main teams
- `GET /api/teams/{id}/sub-teams` - Get sub-teams
- `POST /api/teams` - Create team (Admin/Manager)
- `PUT /api/teams/{id}` - Update team (Admin/Manager)
- `DELETE /api/teams/{id}` - Delete team (Admin only)

### CSV Upload
- `POST /api/csv/upload/users` - Bulk upload users via CSV (Admin only)

### Skills
- `GET /api/skills` - Get all skills
- `GET /api/skills/{id}` - Get skill by ID
- `GET /api/skills/user/{userId}` - Get skills by user ID
- `GET /api/skills/username/{username}` - Get skills by username
- `GET /api/skills/search?name=&type=&proficiency=` - Search skills
- `POST /api/skills/user/{userId}` - Create skill for user
- `PUT /api/skills/{id}` - Update skill
- `DELETE /api/skills/{id}` - Delete skill

### Endorsements
- `GET /api/endorsements` - Get all endorsements
- `GET /api/endorsements/{id}` - Get endorsement by ID
- `GET /api/endorsements/skill/{skillId}` - Get endorsements by skill
- `GET /api/endorsements/user/{userId}` - Get endorsements received by user
- `GET /api/endorsements/given-by/{userId}` - Get endorsements given by user
- `POST /api/endorsements/endorser/{endorserId}` - Create endorsement
- `DELETE /api/endorsements/{id}` - Delete endorsement

### Search
- `POST /api/search` - Search users by skills, team, role, name (with ranking)

### Analytics
- `GET /api/analytics` - Get analytics dashboard (Admin/Manager/Team Lead only)

### Export
- `GET /api/export/users` - Export users to CSV (Admin/Manager)
- `GET /api/export/skills` - Export skills to CSV (Admin/Manager)
- `GET /api/export/endorsements` - Export endorsements to CSV (Admin/Manager)
- `GET /api/export/all` - Export all data to CSV (Admin/Manager)

### CSV Format for User Upload
```csv
username,password,name,contactInfo,role,photoUrl,mainTeam,subTeam
john.doe,password123,John Doe,john@example.com,EMPLOYEE,,Engineering,Backend
```

## Security
- JWT-based authentication
- Role-based access control (ADMIN, MANAGER, TEAM_LEAD, EMPLOYEE)
- CORS enabled for `http://localhost:4200`
- Stateless sessions

## Project Structure
```
backend/
├── src/
│   └── main/
│       ├── java/com/officeknowledgemap/
│       │   ├── bootstrap/         # Admin bootstrap
│       │   ├── config/            # Security configuration
│       │   ├── controller/        # REST controllers (9 controllers)
│       │   ├── dto/               # Request/Response DTOs (15+ DTOs)
│       │   ├── exception/         # Exception handlers
│       │   ├── model/             # JPA entities (User, Team, Skill, Endorsement)
│       │   ├── repository/        # JPA repositories (4 repositories)
│       │   ├── security/          # JWT utilities
│       │   └── service/           # Business logic (8 services)
│       └── resources/
│           └── application.properties
└── pom.xml
```

## Key Features

### Authentication & Authorization
- JWT-based stateless authentication
- BCrypt password encryption
- Role-based access control (ADMIN, MANAGER, TEAM_LEAD, EMPLOYEE)
- Method-level security with @PreAuthorize

### User & Team Management
- Complete CRUD operations for users and teams
- Hierarchical team structure (main teams + sub-teams)
- Role and team assignment

### Skills & Endorsements
- Custom skill creation with types and proficiency levels
- Skill types: TECHNICAL, SOFT_SKILL, DOMAIN_KNOWLEDGE, CERTIFICATION
- Proficiency levels: BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
- Endorsement validation (no self-endorsement, no duplicates)
- Endorsement tracking with comments

### Search & Analytics
- Multi-criteria search (skill name, type, proficiency, team, role, user name)
- Ranking by endorsement count
- Top skills analysis
- Team statistics
- Endorsement trends
- Skill type and proficiency distributions

### Data Import/Export
- CSV bulk user import with validation
- CSV export for users, skills, endorsements, and combined data
- Apache Commons CSV integration

