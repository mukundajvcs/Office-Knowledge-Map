import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Team, TeamHierarchy } from '../models/team.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private apiUrl = `${environment.apiUrl}/teams`;

  constructor(private http: HttpClient) {}

  getAllTeams(): Observable<Team[]> {
    return this.http.get<Team[]>(this.apiUrl);
  }

  getMainTeams(): Observable<Team[]> {
    return this.http.get<Team[]>(`${this.apiUrl}/main`);
  }

  getSubTeams(mainTeamId: string | number): Observable<Team[]> {
    return this.http.get<Team[]>(`${this.apiUrl}/${mainTeamId}/sub-teams`);
  }

  getTeamById(id: string | number): Observable<Team> {
    return this.http.get<Team>(`${this.apiUrl}/${id}`);
  }

  createMainTeam(team: Omit<Team, 'id' | 'parentTeamId' | 'isMainTeam'>): Observable<Team> {
    return this.http.post<Team>(this.apiUrl, {
      ...team,
      isMainTeam: true
    });
  }

  createSubTeam(mainTeamId: string | number, team: Omit<Team, 'id' | 'parentTeamId' | 'isMainTeam'>): Observable<Team> {
    return this.http.post<Team>(this.apiUrl, {
      ...team,
      parentTeamId: mainTeamId,
      isMainTeam: false
    });
  }

  updateTeam(id: string | number, team: Partial<Team>): Observable<Team> {
    return this.http.put<Team>(`${this.apiUrl}/${id}`, team);
  }

  deleteTeam(id: string | number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getTeamHierarchy(): Observable<TeamHierarchy[]> {
    return this.http.get<TeamHierarchy[]>(`${this.apiUrl}/hierarchy`);
  }

  getTeamMembers(teamId: string | number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${teamId}/members`);
  }
}
