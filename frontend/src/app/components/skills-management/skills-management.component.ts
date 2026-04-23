import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { SkillService } from '../../services/skill.service';
import { AuthService } from '../../services/auth.service';
import { Skill, SkillType, SkillProficiency } from '../../models/skill.model';
import { Observable, startWith, map } from 'rxjs';

@Component({
  selector: 'app-skills-management',
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
    MatTableModule,
    MatChipsModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatAutocompleteModule
  ],
  templateUrl: './skills-management.component.html',
  styleUrls: ['./skills-management.component.scss']
})
export class SkillsManagementComponent implements OnInit {
  skillForm: FormGroup;
  skills: Skill[] = [];
  isLoading = false;
  isSaving = false;
  skillTypes = Object.values(SkillType);
  proficiencyLevels = Object.values(SkillProficiency);
  allSkillNames: string[] = [];
  filteredSkillNames!: Observable<string[]>;
  
  displayedColumns: string[] = ['skillName', 'skillType', 'proficiency', 'endorsements', 'actions'];

  constructor(
    private fb: FormBuilder,
    private skillService: SkillService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.skillForm = this.fb.group({
      skillName: ['', [Validators.required, Validators.minLength(2)]],
      skillType: [SkillType.TECHNICAL, [Validators.required]],
      proficiency: [SkillProficiency.BEGINNER, [Validators.required]]
    });
  }

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && typeof currentUser.id === 'string') {
      currentUser.id = parseInt(currentUser.id, 10);
      localStorage.setItem('currentUser', JSON.stringify(currentUser));
    }
    
    this.loadUserSkills();
    this.loadSkillNames();
    this.setupAutocomplete();
  }

  setupAutocomplete(): void {
    this.filteredSkillNames = this.skillForm.get('skillName')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filterSkillNames(value || ''))
    );
  }

  private _filterSkillNames(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.allSkillNames.filter(name => 
      name.toLowerCase().includes(filterValue)
    );
  }

  loadSkillNames(): void {
    this.skillService.getAllSkillNames().subscribe({
      next: (names) => {
        this.allSkillNames = names;
      },
      error: (error) => {
        // Silent fail for autocomplete
      }
    });
  }

  loadUserSkills(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.isLoading = true;
      this.skillService.getUserSkills(currentUser.id).subscribe({
        next: (skills) => {
          setTimeout(() => {
            this.skills = [...skills];
            this.isLoading = false;
            this.cdr.markForCheck();
          }, 0);
        },
        error: (error) => {
          this.snackBar.open('Failed to load skills', 'Close', { duration: 3000 });
          this.isLoading = false;
        }
      });
    }
  }

  onAddSkill(): void {
    if (this.skillForm.valid) {
      const currentUser = this.authService.getCurrentUser();
      if (!currentUser || !currentUser.id) {
        this.snackBar.open('User not authenticated', 'Close', { duration: 3000 });
        return;
      }

      this.isSaving = true;
      const formValue = this.skillForm.value;
      
      // Map form field names to API field names
      const skill = {
        name: formValue.skillName,
        type: formValue.skillType,
        proficiency: formValue.proficiency,
        description: formValue.description || '',
        yearsOfExperience: formValue.yearsOfExperience || 0
      };

      this.skillService.addSkill(currentUser.id, skill).subscribe({
        next: (newSkill) => {
          this.snackBar.open('Skill added successfully!', 'Close', { duration: 3000 });
          this.skillForm.reset({
            skillType: SkillType.TECHNICAL,
            proficiency: SkillProficiency.BEGINNER
          });
          this.loadUserSkills();
          this.isSaving = false;
        },
        error: (error: any) => {
          this.snackBar.open(
            error.error?.message || 'Failed to add skill',
            'Close',
            { duration: 5000 }
          );
          this.isSaving = false;
        }
      });
    }
  }

  deleteSkill(skillId: number): void {
    if (confirm('Are you sure you want to delete this skill?')) {
      this.skillService.deleteSkill(skillId).subscribe({
        next: () => {
          this.snackBar.open('Skill deleted successfully', 'Close', { duration: 3000 });
          this.loadUserSkills();
        },
        error: (error) => {
          this.snackBar.open('Failed to delete skill', 'Close', { duration: 3000 });
        }
      });
    }
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

  getProficiencyLabel(proficiency: SkillProficiency): string {
    const labels: { [key in SkillProficiency]: string } = {
      BEGINNER: '⭐',
      INTERMEDIATE: '⭐⭐',
      ADVANCED: '⭐⭐⭐',
      EXPERT: '⭐⭐⭐⭐'
    };
    return labels[proficiency];
  }
}
