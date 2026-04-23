import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { UserRole } from '../models/user.model';

/**
 * Guard to check if user has manager or above role (ADMIN, MANAGER, TEAM_LEAD)
 */
export const managerOrAboveGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  const currentUser = authService.getCurrentUser();
  
  if (currentUser && 
      (currentUser.role === UserRole.ADMIN || 
       currentUser.role === UserRole.MANAGER || 
       currentUser.role === UserRole.TEAM_LEAD)) {
    return true;
  }
  
  // Redirect to profile if user is employee
  router.navigate(['/profile']);
  return false;
};

/**
 * Guard to check if user is admin
 */
export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  const currentUser = authService.getCurrentUser();
  
  if (currentUser && currentUser.role === UserRole.ADMIN) {
    return true;
  }
  
  // Redirect to profile if user is not admin
  router.navigate(['/profile']);
  return false;
};
