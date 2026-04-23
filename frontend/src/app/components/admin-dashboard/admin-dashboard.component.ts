import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { UserService } from '../../services/user.service';
import { TeamService } from '../../services/team.service';
import { User, UserRole } from '../../models/user.model';
import { Team } from '../../models/team.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTabsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatDialogModule
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  userForm: FormGroup;
  teamForm: FormGroup;
  csvFile: File | null = null;
  users: User[] = [];
  teams: Team[] = [];
  mainTeams: Team[] = [];
  subTeams: Team[] = [];
  isLoadingUsers = false;
  isLoadingTeams = false;
  isSavingUser = false;
  isSavingTeam = false;
  isUploadingCsv = false;

  userRoles = Object.values(UserRole);
  displayedColumns: string[] = ['username', 'name', 'role', 'team', 'contactInfo', 'actions'];
  teamDisplayedColumns: string[] = ['name', 'description', 'type', 'actions'];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private teamService: TeamService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      name: ['', [Validators.required]],
      role: [UserRole.EMPLOYEE, [Validators.required]],
      contactInfo: ['', [Validators.required, Validators.email]],
      mainTeamId: [''],
      subTeamId: ['']
    });

    this.teamForm = this.fb.group({
      name: ['', [Validators.required]],
      description: [''],
      isMainTeam: [true],
      parentTeamId: [null]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
    this.loadTeams();
    this.setupTeamFormListeners();
    this.setupUserFormListeners();
  }

  setupUserFormListeners(): void {
    // Load sub-teams when main team is selected
    this.userForm.get('mainTeamId')?.valueChanges.subscribe(mainTeamId => {
      this.userForm.patchValue({ subTeamId: null });
      if (mainTeamId) {
        this.loadSubTeams(mainTeamId);
      } else {
        this.subTeams = [];
      }
    });
  }

  setupTeamFormListeners(): void {
    // Clear parentTeamId when switching to main team
    this.teamForm.get('isMainTeam')?.valueChanges.subscribe(isMainTeam => {
      if (isMainTeam) {
        this.teamForm.patchValue({ parentTeamId: null });
      }
    });
  }

  loadSubTeams(mainTeamId: number): void {
    this.teamService.getSubTeams(mainTeamId).subscribe({
      next: (teams) => {
        this.subTeams = teams;
      },
      error: (error) => {
        console.error('Failed to load sub-teams', error);
        this.subTeams = [];
      }
    });
  }

  loadUsers(): void {
    this.isLoadingUsers = true;
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = [...users]; // Create new array reference to trigger change detection
        this.isLoadingUsers = false;
      },
      error: (error) => {
        this.snackBar.open('Failed to load users', 'Close', { duration: 3000 });
        this.isLoadingUsers = false;
      }
    });
  }

  loadTeams(): void {
    this.isLoadingTeams = true;
    this.teamService.getAllTeams().subscribe({
      next: (teams) => {
        this.teams = [...teams]; // Create new array reference to trigger change detection
        this.mainTeams = teams.filter(t => t.isMainTeam);
        this.isLoadingTeams = false;
      },
      error: (error) => {
        this.snackBar.open('Failed to load teams', 'Close', { duration: 3000 });
        this.isLoadingTeams = false;
      }
    });
  }

  onCreateUser(): void {
    if (this.userForm.valid) {
      this.isSavingUser = true;
      const user: User = this.userForm.value;
      
      this.userService.createUser(user).subscribe({
        next: (createdUser) => {
          this.snackBar.open('User created successfully!', 'Close', { duration: 3000 });
          this.userForm.reset({ role: UserRole.EMPLOYEE, isMainTeam: true });
          this.loadUsers();
          this.isSavingUser = false;
        },
        error: (error) => {
          this.snackBar.open(
            error.error?.message || 'Failed to create user',
            'Close',
            { duration: 5000 }
          );
          this.isSavingUser = false;
        }
      });
    }
  }

  onCreateTeam(): void {
    if (this.teamForm.valid) {
      this.isSavingTeam = true;
      const teamData = this.teamForm.value;
      
      if (teamData.isMainTeam) {
        // Create main team
        this.teamService.createMainTeam(teamData).subscribe({
          next: (team) => {
            this.snackBar.open('Main team created successfully!', 'Close', { duration: 3000 });
            this.teamForm.reset({ isMainTeam: true });
            this.loadTeams();
            this.isSavingTeam = false;
          },
          error: (error) => {
            this.snackBar.open('Failed to create main team', 'Close', { duration: 5000 });
            this.isSavingTeam = false;
          }
        });
      } else {
        // Create sub-team
        if (!teamData.parentTeamId) {
          this.snackBar.open('Please select a parent team for the sub-team', 'Close', { duration: 3000 });
          this.isSavingTeam = false;
          return;
        }
        
        this.teamService.createSubTeam(teamData.parentTeamId, teamData).subscribe({
          next: (team) => {
            this.snackBar.open('Sub-team created successfully!', 'Close', { duration: 3000 });
            this.teamForm.reset({ isMainTeam: true });
            this.loadTeams();
            this.isSavingTeam = false;
          },
          error: (error) => {
            this.snackBar.open('Failed to create sub-team', 'Close', { duration: 5000 });
            this.isSavingTeam = false;
          }
        });
      }
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.csvFile = input.files[0];
    }
  }

  onUploadCsv(): void {
    if (this.csvFile) {
      this.isUploadingCsv = true;
      
      this.userService.uploadUsersCsv(this.csvFile).subscribe({
        next: (result) => {
          this.snackBar.open(
            `Upload complete! Success: ${result.successCount}, Failed: ${result.failedCount}`,
            'Close',
            { duration: 5000 }
          );
          this.csvFile = null;
          this.loadUsers();
          this.isUploadingCsv = false;
        },
        error: (error) => {
          this.snackBar.open('CSV upload failed', 'Close', { duration: 5000 });
          this.isUploadingCsv = false;
        }
      });
    }
  }

  deleteUser(userId: string): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          this.snackBar.open('User deleted successfully', 'Close', { duration: 3000 });
          this.loadUsers();
        },
        error: (error) => {
          this.snackBar.open('Failed to delete user', 'Close', { duration: 3000 });
        }
      });
    }
  }

  deleteTeam(teamId: string): void {
    if (confirm('Are you sure you want to delete this team?')) {
      this.teamService.deleteTeam(teamId).subscribe({
        next: () => {
          this.snackBar.open('Team deleted successfully', 'Close', { duration: 3000 });
          this.loadTeams();
        },
        error: (error) => {
          this.snackBar.open('Failed to delete team', 'Close', { duration: 3000 });
        }
      });
    }
  }

  getTeamDisplay(user: User): string {
    if (user.mainTeamName && user.subTeamName) {
      return `${user.mainTeamName}_${user.subTeamName}`;
    } else if (user.mainTeamName) {
      return user.mainTeamName;
    } else if (user.subTeamName) {
      return user.subTeamName;
    }
    return '-';
  }
}
