import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { API_URLS } from '../app.config';

@Injectable({
  providedIn: 'root'
})
export class RootsService {

  constructor(private http: HttpClient) {
  }

  adminRootsRequest(): Observable<any> {
    return this.http.post<any>(`${API_URLS.ROOTS}/request`, {});
  }

  getRoles(): Observable<any> {
    return this.http.get<any>(`${API_URLS.ROOTS}`, {}).pipe(
      tap(roles => {
        if (roles.indexOf("ADMIN") != -1) {
          localStorage.setItem('isAdmin', "true");
        }
      })
    );
  }

  getRootsRequests(): Observable<any> {
    return this.http.get<any>(`${API_URLS.ROOTS}/request`);
  }

  aproveRootsRequest(id: number): Observable<any> {
    return this.http.put<any>(`${API_URLS.ROOTS}/request/aprove/${id}`, {})
  }

  declineRootsRequest(id: number): Observable<any> {
    return this.http.put<any>(`${API_URLS.ROOTS}/request/decline/${id}`, {})
  }

}
