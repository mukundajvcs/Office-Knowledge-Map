# Frontend-Backend Integration

## Integration Completed

### Environment Configuration
- Created `environment.ts` and `environment.prod.ts` with backend API URL: `http://localhost:8080/api`

### HTTP Interceptor
- Created `AuthInterceptor` to automatically add JWT Bearer token to all HTTP requests
- Configured in `app.config.ts` as global HTTP interceptor

### Services Updated
All services now integrated with backend API:

#### AuthService
- Updated to handle JWT token from backend `AuthResponse` (token, username, name, role)
- Stores token in localStorage and includes in all requests via interceptor
- Endpoints: `/api/auth/register`, `/api/auth/login`

#### UserService
- Updated to use `/api/users` endpoints
- CSV upload endpoint: `/api/csv/upload/users`

#### SkillService
- Updated to use `/api/skills` endpoints
- Create skill: `POST /api/skills/user/{userId}`
- Get user skills: `GET /api/skills/user/{userId}`
- Get by username: `GET /api/skills/username/{username}`

#### EndorsementService
- Updated to use `/api/endorsements` endpoints
- Create endorsement: `POST /api/endorsements/endorser/{endorserId}`
- Get user endorsements: `GET /api/endorsements/user/{userId}`
- Get given by user: `GET /api/endorsements/given-by/{userId}`
- Get skill endorsements: `GET /api/endorsements/skill/{skillId}`

#### TeamService
- Updated to use `/api/teams` endpoints
- Get main teams: `GET /api/teams/main`
- Get sub-teams: `GET /api/teams/{id}/sub-teams`

#### SearchService
- Updated to use `POST /api/search` with request body

#### AnalyticsService
- Updated to use `GET /api/analytics` for complete dashboard
- Export endpoints:
  - `GET /api/export/users`
  - `GET /api/export/skills`
  - `GET /api/export/endorsements`
  - `GET /api/export/all`

### API Alignment
Frontend services now match backend DTOs:
- AuthResponse: token, username, name, role
- All endpoints use correct HTTP methods (GET, POST, PUT, DELETE)
- Request/response structures aligned with backend controllers

### Authentication Flow
1. User logs in → receives JWT token
2. Token stored in localStorage
3. AuthInterceptor adds `Authorization: Bearer {token}` header to all requests
4. Backend validates token for protected endpoints

## Testing
Start both servers and test at:
- Frontend: http://localhost:4200
- Backend: http://localhost:8080/api
- Admin credentials: admin / admin123
