import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserRole } from '../models/user.model';
import { CsvUploadResult } from '../models/csv-upload.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getUserById(id: string | number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  updateUser(id: string | number, user: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, user);
  }

  deleteUser(id: string | number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  uploadUsersCsv(file: File): Observable<CsvUploadResult> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<CsvUploadResult>(`${environment.apiUrl}/csv/upload/users`, formData);
  }

  updateUserRole(userId: string | number, role: UserRole): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/${userId}/role`, { role });
  }

  assignUserToTeam(userId: string | number, mainTeamId: string | number, subTeamId?: string | number): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/${userId}/team`, {
      mainTeamId,
      subTeamId
    });
  }

  uploadPhoto(userId: string | number, file: File): Observable<User> {
    const formData = new FormData();
    formData.append('photo', file);
    return this.http.post<User>(`${this.apiUrl}/${userId}/photo`, formData);
  }
}
