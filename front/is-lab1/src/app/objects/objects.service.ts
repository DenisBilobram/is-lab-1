import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URLS } from '../app.config';
import { Observable } from 'rxjs';
import { EntityDataMap, EntityType } from './objects-manager/objects-manager.component';
import { Car } from './models/car.model';
import { HumanBeing } from './models/human-being.model';
import { Coordinates } from './models/coordinates.model';
import { WeaponType } from './models/weapon-type.enum';
import { urlencoded } from 'express';

@Injectable({
  providedIn: 'root'
})
export class ObjectsService {

  constructor(private http: HttpClient) {}

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

  countImpactSpeed(impactSpeed: number): Observable<any> {
    return this.http.get(`${API_URLS.OBJECTS}/human-being/count/impact-speed/${impactSpeed}`); 
  }

  findByNameContaining(str: string): Observable<any> {
    return this.http.get(`${API_URLS.OBJECTS}/human-being/search/name?substring=${encodeURI(str)}`); 
  }

  findByWeaponTypeGreaterThan(weaponType: WeaponType): Observable<any> {
    return this.http.get(`${API_URLS.OBJECTS}/human-being/search/weapon-type?weaponType=${encodeURI(weaponType)}`); 
  }

  deleteAllWithoutToothpick() {
    return this.http.delete(`${API_URLS.OBJECTS}/human-being/delete/without-toothpick`)
  }

  updateMoodForAll() {
    return this.http.put(`${API_URLS.OBJECTS}/human-being/update/mood-sorrow`, {});
  }

  updateTracks(objects: HumanBeing[] | Car[] | Coordinates[]): any[] {
    let track: number = 0;
    objects.forEach(object => {
      object.track = track++;
    })

    return objects;
  }

  perfromeEvent(objectsList: any[], object: any): any[] {
    let currentList: any[] = [...objectsList];
  
    switch (object.type) {
      case "UPDATE":
        const indexToUpdate = currentList.findIndex(el => el.id === object.id);
        if (indexToUpdate !== -1) {
          Object.assign(currentList[indexToUpdate], object);
        }
        break;
      case "CREATE":
        currentList.push(object);
        break;
      case "DELETE":
        currentList = currentList.filter(el => el.id !== object.id);
        break;
    }

    return this.updateTracks(currentList);
  }

}
