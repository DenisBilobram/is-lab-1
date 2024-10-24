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
        this.setData(response.username, response.email, response.jwt, response.isAdmin);
      }),
    );
  }

  register(credentials: { username: string; password: string; email: string}): Observable<any> {
    return this.http.post<any>(`${API_URLS.AUTH}/register`, credentials, {withCredentials: true}).pipe(
      tap(response => {
        this.setData(response.username, response.email, response.jwt, response.isAdmin);
      })
    );
  }

  adminRootsRequest(): Observable<any> {
    return this.http.post<any>(`${API_URLS.AUTH}/roots`, {});
  }

  getRoles(): Observable<any> {
    return this.http.get<any>(`${API_URLS.AUTH}/roots`, {});
  }

  setData(username: string, email: string, jwt: string, isAdmin: string) {
    localStorage.setItem('username', username);
    localStorage.setItem('email', email);
    localStorage.setItem('authToken', jwt);
    localStorage.setItem('isAdmin', isAdmin);
  }
}
