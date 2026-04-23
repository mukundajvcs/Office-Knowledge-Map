import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SearchFilters, SearchResult } from '../models/search.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private apiUrl = `${environment.apiUrl}/search`;

  constructor(private http: HttpClient) {}

  // Search for users by skills and filters
  searchUsers(filters: SearchFilters): Observable<SearchResult[]> {
    return this.http.post<SearchResult[]>(this.apiUrl, filters);
  }

  // Quick search by skill name only
  quickSearch(skillName: string): Observable<SearchResult[]> {
    return this.searchUsers({ skillName });
  }

  // Get trending skills
  getTrendingSkills(limit: number = 10): Observable<{ skillName: string; userCount: number }[]> {
    return this.http.get<{ skillName: string; userCount: number }[]>(
      `${this.apiUrl}/trending?limit=${limit}`
    );
  }
}
