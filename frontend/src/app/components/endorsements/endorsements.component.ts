import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTabsModule } from '@angular/material/tabs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatBadgeModule } from '@angular/material/badge';
import { EndorsementService } from '../../services/endorsement.service';
import { SkillService } from '../../services/skill.service';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { Endorsement, EndorsementSummary, UserEndorsementGroup } from '../../models/endorsement.model';
import { User, UserRole } from '../../models/user.model';
import { Skill } from '../../models/skill.model';

@Component({
  selector: 'app-endorsements',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatChipsModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTabsModule,
    MatExpansionModule,
    MatBadgeModule
  ],
  templateUrl: './endorsements.component.html',
  styleUrls: ['./endorsements.component.scss']
})
export class EndorsementsComponent implements OnInit {
  receivedEndorsementsSummary: EndorsementSummary[] = [];
  receivedEndorsements: Endorsement[] = [];
  givenEndorsements: Endorsement[] = [];
  givenEndorsementsGrouped: UserEndorsementGroup[] = [];
  teamMembers: User[] = [];
  filteredTeamMembers: User[] = [];
  memberSkills: Map<number, Skill[]> = new Map();
  isLoadingReceived = false;
  isLoadingGiven = false;
  isLoadingMembers = false;
  currentUserId: number = 0;
  currentUserRole: UserRole = UserRole.EMPLOYEE;
  currentUser: User | null = null;
  
  // Expose UserRole enum to template
  readonly UserRole = UserRole;

  constructor(
    private endorsementService: EndorsementService,
    private skillService: SkillService,
    private userService: UserService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.currentUserId = currentUser.id;
      this.currentUserRole = currentUser.role;
      
      // Fetch full user details including team information
      this.userService.getUserById(this.currentUserId).subscribe({
        next: (user) => {
          this.currentUser = user;
          this.loadReceivedEndorsements();
          
          // Only load given endorsements and team members if user can endorse
          if (this.currentUserRole !== UserRole.EMPLOYEE) {
            this.loadGivenEndorsements();
            this.loadTeamMembers();
          }
        },
        error: (error) => {
          this.snackBar.open('Failed to load user details', 'Close', { duration: 3000 });
        }
      });
    }
  }

  loadReceivedEndorsements(): void {
    this.isLoadingReceived = true;
    this.endorsementService.getUserEndorsements(this.currentUserId).subscribe({
      next: (endorsements) => {
        this.receivedEndorsements = endorsements;
        // Group endorsements by skill to create summary
        this.receivedEndorsementsSummary = this.groupEndorsementsBySkill(endorsements);
        this.isLoadingReceived = false;
      },
      error: (error: any) => {
        this.snackBar.open('Failed to load received endorsements', 'Close', { duration: 3000 });
        this.isLoadingReceived = false;
      }
    });
  }

  private groupEndorsementsBySkill(endorsements: Endorsement[]): EndorsementSummary[] {
    const skillMap = new Map<number, EndorsementSummary>();
    
    endorsements.forEach(endorsement => {
      const skillId = endorsement.skillId;
      if (!skillMap.has(skillId)) {
        skillMap.set(skillId, {
          skillId: skillId,
          skillName: endorsement.skillName || 'Unknown Skill',
          totalEndorsements: 0,
          endorsers: []
        });
      }
      
      const summary = skillMap.get(skillId)!;
      summary.totalEndorsements++;
      summary.endorsers.push({
        id: endorsement.endorserId,
        name: endorsement.endorserName || 'Unknown',
        timestamp: endorsement.createdAt || new Date()
      });
    });
    
    return Array.from(skillMap.values());
  }

  loadGivenEndorsements(): void {
    this.isLoadingGiven = true;
    this.endorsementService.getEndorsementsGivenByUser(this.currentUserId).subscribe({
      next: (endorsements) => {
        this.givenEndorsements = endorsements;
        this.givenEndorsementsGrouped = this.groupEndorsementsByUser(endorsements);
        this.isLoadingGiven = false;
      },
      error: (error) => {
        this.snackBar.open('Failed to load given endorsements', 'Close', { duration: 3000 });
        this.isLoadingGiven = false;
      }
    });
  }

  private groupEndorsementsByUser(endorsements: Endorsement[]): UserEndorsementGroup[] {
    const userMap = new Map<number, UserEndorsementGroup>();
    
    endorsements.forEach(endorsement => {
      const userId = endorsement.endorsedUserId;
      if (!userMap.has(userId)) {
        userMap.set(userId, {
          userId: userId,
          userName: endorsement.endorsedUserName || 'Unknown User',
          skills: []
        });
      }
      
      const group = userMap.get(userId)!;
      group.skills.push({
        skillId: endorsement.skillId,
        skillName: endorsement.skillName || 'Unknown Skill',
        endorsementDate: endorsement.createdAt || new Date()
      });
    });
    
    return Array.from(userMap.values());
  }

  loadTeamMembers(): void {
    this.isLoadingMembers = true;
    // Load all users and filter based on team membership and role-based endorsement permissions
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        // Filter out current user
        this.teamMembers = users.filter(u => u.id !== this.currentUserId);
        
        // Filter to only show users from the same team
        const sameTeamMembers = this.teamMembers.filter(u => this.isSameTeam(u));
        
        // Further filter to only show users that current user can endorse based on role
        this.filteredTeamMembers = sameTeamMembers.filter(u => this.canEndorseUser(u.role));
        
        this.loadMemberSkills();
        this.isLoadingMembers = false;
      },
      error: (error) => {
        this.snackBar.open('Failed to load team members', 'Close', { duration: 3000 });
        this.isLoadingMembers = false;
      }
    });
  }

  loadMemberSkills(): void {
    this.filteredTeamMembers.forEach(member => {
      if (member.id) {
        this.skillService.getUserSkills(member.id).subscribe({
          next: (skills) => {
            this.memberSkills.set(member.id!, skills);
          },
          error: (error) => {
            console.error(`Failed to load skills for user ${member.id}`, error);
          }
        });
      }
    });
  }

  getMemberSkills(userId: number): Skill[] {
    return this.memberSkills.get(userId) || [];
  }

  endorseSkill(userId: number, skillId: number, skillName: string): void {
    // Current user is the endorser
    this.endorsementService.endorseSkill(this.currentUserId, skillId).subscribe({
      next: (endorsement) => {
        this.snackBar.open(`Successfully endorsed ${skillName}!`, 'Close', { duration: 3000 });
        this.loadGivenEndorsements();
        // Reload the member's skills to update endorsement count
        this.skillService.getUserSkills(userId).subscribe({
          next: (skills) => {
            this.memberSkills.set(userId, skills);
          }
        });
      },
      error: (error: any) => {
        this.snackBar.open(
          error.error?.message || 'Failed to endorse skill. You may not have permission.',
          'Close',
          { duration: 5000 }
        );
      }
    });
  }

  hasEndorsed(skillId: number): boolean {
    return this.givenEndorsements.some(e => e.skillId === skillId);
  }

  getTotalEndorsements(): number {
    return this.receivedEndorsementsSummary.reduce((sum, skill) => sum + skill.totalEndorsements, 0);
  }

  /**
   * Checks if a user belongs to the same team as the current user.
   * Users are considered in the same team if they share:
   * - The same main team (if no sub-team)
   * - OR the same sub-team (if sub-team is assigned)
   */
  isSameTeam(user: User): boolean {
    if (!this.currentUser) {
      return false;
    }
    
    // If both have sub-teams, check if they match
    if (this.currentUser.subTeamId && user.subTeamId) {
      return this.currentUser.subTeamId === user.subTeamId;
    }
    
    // If no sub-teams, check main team
    if (this.currentUser.mainTeamId && user.mainTeamId) {
      return this.currentUser.mainTeamId === user.mainTeamId;
    }
    
    return false;
  }
  
  /**
   * Checks if current user can endorse a user with the given role.
   * Role hierarchy:
   * - ADMIN can endorse MANAGER, TEAM_LEAD, EMPLOYEE
   * - MANAGER can endorse TEAM_LEAD, EMPLOYEE
   * - TEAM_LEAD can endorse EMPLOYEE
   * - EMPLOYEE cannot endorse anyone
   */
  canEndorseUser(targetRole: UserRole): boolean {
    switch (this.currentUserRole) {
      case UserRole.ADMIN:
        return targetRole === UserRole.MANAGER 
            || targetRole === UserRole.TEAM_LEAD 
            || targetRole === UserRole.EMPLOYEE;
      
      case UserRole.MANAGER:
        return targetRole === UserRole.TEAM_LEAD 
            || targetRole === UserRole.EMPLOYEE;
      
      case UserRole.TEAM_LEAD:
        return targetRole === UserRole.EMPLOYEE;
      
      case UserRole.EMPLOYEE:
        return false;
      
      default:
        return false;
    }
  }

  getUserInitials(name: string): string {
    return name
      .split(' ')
      .map(n => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  }
}
