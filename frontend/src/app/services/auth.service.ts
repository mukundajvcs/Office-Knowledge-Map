import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { User, UserRole } from '../models/user.model';
import { environment } from '../../environments/environment';

interface AuthResponse {
  token: string;
  id: string;
  username: string;
  name: string;
  role: UserRole;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // Load user from localStorage if exists
    const storedUser = localStorage.getItem('currentUser');
    const storedToken = localStorage.getItem('token');
    if (storedUser && storedToken) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  register(username: string, password: string, name: string, contactInfo: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, {
      username,
      password,
      name,
      contactInfo,
      role: UserRole.EMPLOYEE
    }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        const user: User = {
          id: parseInt(response.id, 10),
          username: response.username,
          name: response.name,
          role: response.role,
          contactInfo: contactInfo,
          createdAt: new Date(),
          updatedAt: new Date()
        };
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
      })
    );
  }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        const user: User = {
          id: parseInt(response.id, 10),
          username: response.username,
          name: response.name,
          role: response.role,
          contactInfo: '',
          createdAt: new Date(),
          updatedAt: new Date()
        };
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): boolean {
    return !!this.currentUserSubject.value;
  }

  isAdmin(): boolean {
    const user = this.currentUserSubject.value;
    return user?.role === UserRole.ADMIN;
  }

  isManagerOrAbove(): boolean {
    const user = this.currentUserSubject.value;
    return user?.role === UserRole.ADMIN || 
           user?.role === UserRole.MANAGER || 
           user?.role === UserRole.TEAM_LEAD;
  }
}
