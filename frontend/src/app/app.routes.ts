import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { TeamHierarchyComponent } from './components/team-hierarchy/team-hierarchy.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { SkillsManagementComponent } from './components/skills-management/skills-management.component';
import { EndorsementsComponent } from './components/endorsements/endorsements.component';
import { SkillSearchComponent } from './components/skill-search/skill-search.component';
import { AnalyticsDashboardComponent } from './components/analytics-dashboard/analytics-dashboard.component';
import { adminGuard, managerOrAboveGuard } from './guards/role.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: AdminDashboardComponent, canActivate: [adminGuard] },
  { path: 'team-hierarchy', component: TeamHierarchyComponent, canActivate: [managerOrAboveGuard] },
  { path: 'profile', component: UserProfileComponent },
  { path: 'skills', component: SkillsManagementComponent },
  { path: 'endorsements', component: EndorsementsComponent },
  { path: 'search', component: SkillSearchComponent, canActivate: [managerOrAboveGuard] },
  { path: 'analytics', component: AnalyticsDashboardComponent, canActivate: [managerOrAboveGuard] },
  { path: '**', redirectTo: '/login' }
];
