import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Endorsement, EndorsementSummary } from '../models/endorsement.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EndorsementService {
  private apiUrl = `${environment.apiUrl}/endorsements`;

  constructor(private http: HttpClient) {}

  // Create an endorsement
  endorseSkill(endorserId: string | number, skillId: string | number, comment?: string): Observable<Endorsement> {
    return this.http.post<Endorsement>(`${this.apiUrl}/endorser/${endorserId}`, {
      skillId,
      comment
    });
  }

  // Get all endorsements for a user
  getUserEndorsements(userId: string | number): Observable<Endorsement[]> {
    return this.http.get<Endorsement[]>(`${this.apiUrl}/user/${userId}`);
  }

  // Get endorsements for a specific skill
  getSkillEndorsements(skillId: string | number): Observable<Endorsement[]> {
    return this.http.get<Endorsement[]>(`${this.apiUrl}/skill/${skillId}`);
  }

  // Get endorsements given by a user
  getEndorsementsGivenByUser(userId: string | number): Observable<Endorsement[]> {
    return this.http.get<Endorsement[]>(`${this.apiUrl}/given-by/${userId}`);
  }
}
