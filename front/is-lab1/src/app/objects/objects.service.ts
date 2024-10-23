import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URLS } from '../app.config';
import { Observable } from 'rxjs';
import { EntityDataMap, EntityType } from './objects-manager/objects-manager.component';
import { Car } from './models/car.model';
import { HumanBeing } from './models/human-being.model';
import { Coordinates } from './models/coordinates.model';
import { SseService } from './sse.service';

@Injectable({
  providedIn: 'root'
})
export class ObjectsService {

  constructor(private http: HttpClient, private sseService: SseService) {}

  getObject(type: EntityType, objectId: number): Observable<any> {
    return this.http.get(`${API_URLS.OBJECTS}/${type}/${objectId}`);
  }

  getObjects(type: EntityType, size: number, page: number): Observable<any> {
    return this.http.get(`${API_URLS.OBJECTS}/${type}?size=${size}&page=${page}`);
  }

  getAllObjects(type: EntityType): Observable<any> {
    return this.http.get(`${API_URLS.OBJECTS}/${type}`);
  }

  postObject(type: EntityType, object: Car | HumanBeing | Coordinates): Observable<any> {
    return this.http.post(`${API_URLS.OBJECTS}/${type}`, object, {});
  }

  updateObject(type: EntityType, objectId: number, data: any): Observable<any> {
    return this.http.put(`${API_URLS.OBJECTS}/${type}/${objectId}`, data);
  }

  deleteObject(type: EntityType, objectId: number): Observable<any> {
    return this.http.delete(`${API_URLS.OBJECTS}/${type}/${objectId}`);
  }

  subscribeToUpdates(type: EntityType): Observable<any> {
    return this.sseService.getServerSentEvent(`${API_URLS.OBJECTS}/${type}/subscribe`);
  }

}
