import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URLS } from '../app.config';
import { catchError, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }) {
    return this.http.post<any>(`${API_URLS.AUTH}/login`, credentials).pipe(
      tap(response => {
        this.setData(response.username, response.email, response.jwt);
      }),
    );
  }

  register(credentials: { username: string; password: string; email: string}) {
    return this.http.post<any>(`${API_URLS.AUTH}/register`, credentials).pipe(
      tap(response => {
        this.setData(response.username, response.email, response.jwt);
      })
    );
  }

  setData(username: string, email: string, jwt: string) {
    localStorage.setItem('username', username)
    localStorage.setItem('email', email)
    localStorage.setItem('authToken', jwt)
  }
}
