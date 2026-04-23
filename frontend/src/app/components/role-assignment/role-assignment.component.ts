import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { UserService } from '../../services/user.service';
import { TeamService } from '../../services/team.service';
import { AuthService } from '../../services/auth.service';
import { User, UserRole } from '../../models/user.model';
import { Team } from '../../models/team.model';

@Component({
  selector: 'app-role-assignment',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],
  templateUrl: './role-assignment.component.html',
  styleUrls: ['./role-assignment.component.scss']
})
export class RoleAssignmentComponent implements OnInit {
  assignmentForm: FormGroup;
  users: User[] = [];
  mainTeams: Team[] = [];
  subTeams: Team[] = [];
  selectedUser: User | null = null;
  isLoading = false;
  isSaving = false;
  canAssignRoles = false;

  userRoles = Object.values(UserRole);
  displayedColumns: string[] = ['name', 'username', 'role', 'team', 'actions'];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private teamService: TeamService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {
    this.assignmentForm = this.fb.group({
      userId: ['', [Validators.required]],
      role: ['', [Validators.required]],
      mainTeamId: [''],
      subTeamId: ['']
    });
  }

  ngOnInit(): void {
    this.canAssignRoles = this.authService.isManagerOrAbove();
    if (this.canAssignRoles) {
      this.loadData();
    }
  }

  loadData(): void {
    this.isLoading = true;
    
    // Load users and teams in parallel
    Promise.all([
      this.userService.getAllUsers().toPromise(),
      this.teamService.getMainTeams().toPromise()
    ]).then(([users, teams]) => {
      this.users = users || [];
      this.mainTeams = teams || [];
      this.isLoading = false;
    }).catch(error => {
      this.snackBar.open('Failed to load data', 'Close', { duration: 3000 });
      this.isLoading = false;
    });
  }

  onUserSelected(): void {
    const userId = this.assignmentForm.get('userId')?.value;
    this.selectedUser = this.users.find(u => u.id === userId) || null;
    
    if (this.selectedUser) {
      this.assignmentForm.patchValue({
        role: this.selectedUser.role,
        mainTeamId: this.selectedUser.mainTeamId || '',
        subTeamId: this.selectedUser.subTeamId || ''
      });

      if (this.selectedUser.mainTeamId) {
        this.loadSubTeams(this.selectedUser.mainTeamId);
      }
    }
  }

  onMainTeamSelected(): void {
    const mainTeamId = this.assignmentForm.get('mainTeamId')?.value;
    if (mainTeamId) {
      this.loadSubTeams(mainTeamId);
    } else {
      this.subTeams = [];
      this.assignmentForm.patchValue({ subTeamId: '' });
    }
  }

  loadSubTeams(mainTeamId: string | number): void {
    this.teamService.getSubTeams(mainTeamId).subscribe({
      next: (teams) => {
        this.subTeams = teams;
      },
      error: (error) => {
        this.snackBar.open('Failed to load sub-teams', 'Close', { duration: 3000 });
      }
    });
  }

  onAssignRole(): void {
    if (this.assignmentForm.valid) {
      this.isSaving = true;
      const { userId, role } = this.assignmentForm.value;
      
      this.userService.updateUserRole(userId, role).subscribe({
        next: () => {
          this.snackBar.open('Role updated successfully!', 'Close', { duration: 3000 });
          this.loadData();
          this.isSaving = false;
        },
        error: (error) => {
          this.snackBar.open('Failed to update role', 'Close', { duration: 3000 });
          this.isSaving = false;
        }
      });
    }
  }

  onAssignTeam(): void {
    if (this.assignmentForm.valid) {
      this.isSaving = true;
      const { userId, mainTeamId, subTeamId } = this.assignmentForm.value;
      
      this.userService.assignUserToTeam(userId, mainTeamId, subTeamId || undefined).subscribe({
        next: () => {
          this.snackBar.open('Team assignment updated successfully!', 'Close', { duration: 3000 });
          this.loadData();
          this.assignmentForm.reset();
          this.selectedUser = null;
          this.isSaving = false;
        },
        error: (error) => {
          this.snackBar.open('Failed to assign team', 'Close', { duration: 3000 });
          this.isSaving = false;
        }
      });
    }
  }

  selectUserForEdit(user: User): void {
    this.assignmentForm.patchValue({
      userId: user.id,
      role: user.role,
      mainTeamId: user.mainTeamId || '',
      subTeamId: user.subTeamId || ''
    });
    this.selectedUser = user;

    if (user.mainTeamId) {
      this.loadSubTeams(user.mainTeamId);
    }
  }

  getRoleBadgeClass(role: UserRole): string {
    return `role-${role.toLowerCase()}`;
  }

  getTeamName(teamId?: string | number): string {
    if (!teamId) return 'Not Assigned';
    const team = [...this.mainTeams, ...this.subTeams].find(t => t.id === teamId);
    return team?.name || 'Unknown';
  }
}
