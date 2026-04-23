import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnalyticsDashboard, TopSkill, TeamSkillSummary, EndorsementTrend } from '../models/analytics.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private apiUrl = `${environment.apiUrl}/analytics`;

  constructor(private http: HttpClient) {}

  // Get complete analytics dashboard
  getDashboard(): Observable<AnalyticsDashboard> {
    return this.http.get<AnalyticsDashboard>(this.apiUrl);
  }

  // Export data as CSV
  exportSkillsData(): Observable<Blob> {
    return this.http.get(`${environment.apiUrl}/export/skills`, {
      responseType: 'blob'
    });
  }

  exportEndorsementsData(): Observable<Blob> {
    return this.http.get(`${environment.apiUrl}/export/endorsements`, {
      responseType: 'blob'
    });
  }

  exportAllData(): Observable<Blob> {
    return this.http.get(`${environment.apiUrl}/export/all`, {
      responseType: 'blob'
    });
  }

  exportUsers(): Observable<Blob> {
    return this.http.get(`${environment.apiUrl}/export/users`, {
      responseType: 'blob'
    });
  }
}
