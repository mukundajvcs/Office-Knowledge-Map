import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatBadgeModule } from '@angular/material/badge';
import { SearchService } from '../../services/search.service';
import { TeamService } from '../../services/team.service';
import { SkillService } from '../../services/skill.service';
import { SearchResult, SearchFilters } from '../../models/search.model';
import { Team } from '../../models/team.model';
import { SkillType, SkillProficiency } from '../../models/skill.model';
import { UserRole } from '../../models/user.model';

@Component({
  selector: 'app-skill-search',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatBadgeModule
  ],
  templateUrl: './skill-search.component.html',
  styleUrls: ['./skill-search.component.scss']
})
export class SkillSearchComponent implements OnInit {
  searchForm: FormGroup;
  searchResults: SearchResult[] = [];
  mainTeams: Team[] = [];
  subTeams: Team[] = [];
  isSearching = false;
  hasSearched = false;
  
  skillTypes = Object.values(SkillType);
  proficiencyLevels = Object.values(SkillProficiency);
  userRoles = Object.values(UserRole);

  constructor(
    private fb: FormBuilder,
    private searchService: SearchService,
    private teamService: TeamService,
    private skillService: SkillService
  ) {
    this.searchForm = this.fb.group({
      skillName: [''],
      skillType: [''],
      proficiency: [''],
      mainTeamId: [''],
      subTeamId: [''],
      role: ['']
    });
  }

  ngOnInit(): void {
    this.loadTeams();
    this.setupFormListeners();
  }

  loadTeams(): void {
    this.teamService.getMainTeams().subscribe({
      next: (teams) => {
        this.mainTeams = teams;
      },
      error: (error) => {
        console.error('Failed to load teams', error);
      }
    });
  }

  setupFormListeners(): void {
    this.searchForm.get('mainTeamId')?.valueChanges.subscribe(mainTeamId => {
      if (mainTeamId) {
        this.loadSubTeams(mainTeamId);
      } else {
        this.subTeams = [];
        this.searchForm.patchValue({ subTeamId: '' });
      }
    });
  }

  loadSubTeams(mainTeamId: string): void {
    this.teamService.getSubTeams(mainTeamId).subscribe({
      next: (teams) => {
        this.subTeams = teams;
      },
      error: (error) => {
        console.error('Failed to load sub-teams', error);
      }
    });
  }

  onSearch(): void {
    const filters: SearchFilters = this.buildFilters();
    this.isSearching = true;
    this.hasSearched = true;

    this.searchService.searchUsers(filters).subscribe({
      next: (results) => {
        this.searchResults = results;
        this.isSearching = false;
      },
      error: (error) => {
        console.error('Search failed', error);
        this.isSearching = false;
      }
    });
  }

  buildFilters(): SearchFilters {
    const formValue = this.searchForm.value;
    const filters: SearchFilters = {};

    if (formValue.skillName) filters.skillName = formValue.skillName;
    if (formValue.skillType) filters.skillType = formValue.skillType;
    if (formValue.proficiency) filters.proficiency = formValue.proficiency;
    if (formValue.mainTeamId) filters.mainTeamId = formValue.mainTeamId;
    if (formValue.subTeamId) filters.subTeamId = formValue.subTeamId;
    if (formValue.role) filters.role = formValue.role;

    return filters;
  }

  onReset(): void {
    this.searchForm.reset();
    this.searchResults = [];
    this.hasSearched = false;
    this.subTeams = [];
  }

  getUserInitials(name: string): string {
    return name
      .split(' ')
      .map(n => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  }

  getSkillTypeColor(type: SkillType): string {
    const colors: { [key in SkillType]: string } = {
      TECHNICAL: 'primary',
      SOFT_SKILL: 'accent',
      DOMAIN_KNOWLEDGE: 'warn',
      CERTIFICATION: ''
    };
    return colors[type] || '';
  }

  getProficiencyStars(proficiency: SkillProficiency): string {
    const stars: { [key in SkillProficiency]: string } = {
      BEGINNER: '⭐',
      INTERMEDIATE: '⭐⭐',
      ADVANCED: '⭐⭐⭐',
      EXPERT: '⭐⭐⭐⭐'
    };
    return stars[proficiency];
  }
}
