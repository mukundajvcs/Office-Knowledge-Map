import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Skill, SkillType, SkillProficiency, SkillWithEndorsements } from '../models/skill.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SkillService {
  private apiUrl = `${environment.apiUrl}/skills`;

  constructor(private http: HttpClient) {}

  // Get all skills for a user
  getUserSkills(userId: string | number): Observable<Skill[]> {
    return this.http.get<Skill[]>(`${this.apiUrl}/user/${userId}`);
  }

  // Get a specific skill
  getSkillById(skillId: string | number): Observable<SkillWithEndorsements> {
    return this.http.get<SkillWithEndorsements>(`${this.apiUrl}/${skillId}`);
  }

  // Add a new skill
  addSkill(userId: string | number, skill: Omit<Skill, 'id' | 'createdAt' | 'updatedAt' | 'userId'>): Observable<Skill> {
    return this.http.post<Skill>(`${this.apiUrl}/user/${userId}`, skill);
  }

  // Update a skill
  updateSkill(skillId: string | number, skill: Partial<Skill>): Observable<Skill> {
    return this.http.put<Skill>(`${this.apiUrl}/${skillId}`, skill);
  }

  // Delete a skill
  deleteSkill(skillId: string | number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${skillId}`);
  }

  // Get all unique skill names (for autocomplete)
  getAllSkillNames(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/names`);
  }

  // Get skill type suggestions
  getSkillTypes(): SkillType[] {
    return Object.values(SkillType);
  }

  // Get proficiency levels
  getProficiencyLevels(): SkillProficiency[] {
    return Object.values(SkillProficiency);
  }
}
