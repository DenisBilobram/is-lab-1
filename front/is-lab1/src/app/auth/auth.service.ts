import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URLS } from '../app.config';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post<any>(`${API_URLS.AUTH}/login`, credentials, {withCredentials: true}).pipe(
      tap(response => {
        this.setData(response.username, response.email, response.jwt);
      }),
    );
  }

  register(credentials: { username: string; password: string; email: string}): Observable<any> {
    return this.http.post<any>(`${API_URLS.AUTH}/register`, credentials, {withCredentials: true}).pipe(
      tap(response => {
        this.setData(response.username, response.email, response.jwt);
      })
    );
  }

  forgotPassword(credentials: { email: string }): Observable<any> {
    return this.http.post(`${API_URLS.AUTH}/forgot-password`, credentials, {withCredentials: true});
  }

  resetPassword(credentials: { token: string, newPassword: string}): Observable<any> {
    return this.http.post(`${API_URLS.AUTH}/reset-password`, credentials, {withCredentials: true});
  }

  setData(username: string, email: string, jwt: string) {
    localStorage.setItem('username', username);
    localStorage.setItem('email', email);
    localStorage.setItem('authToken', jwt);
  }
}
