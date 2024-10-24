import { ChangeDetectorRef, Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { HeaderComponent } from '../../shared/header/header.component';
import { HumanBeing } from '../models/human-being.model';
import { Car } from '../models/car.model';
import { Coordinates } from '../models/coordinates.model';
import { AsyncPipe, CommonModule, NgClass} from '@angular/common';
import { ObjectsService } from '../objects.service';
import { ObjectLineComponent } from './object-line/object-line.component';
import { RouterLink } from '@angular/router';
import { OrderModule, OrderPipe } from 'ngx-order-pipe';
import { BehaviorSubject, Subscription } from 'rxjs';
import { TrackFilterPipe } from './track-filter.pipe';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { WebScoketService } from '../web-scoket.service';

export type EntityType = 'human-being' | 'car' | 'coordinates';

export type EntityDataMap = {
  'human-being': HumanBeing;
  'car': Car;
  'coordinates': Coordinates;
};

@Component({
  selector: 'app-objects-manager',
  standalone: true,
  imports: [
    HeaderComponent,
    NgClass,
    ObjectLineComponent,
    RouterLink,
    OrderModule,
    TrackFilterPipe,
    CommonModule,
    AsyncPipe,
    ReactiveFormsModule
  ],
  templateUrl: './objects-manager.component.html',
  styleUrls: ['./objects-manager.component.scss']
})
export class ObjectsManagerComponent implements OnInit  {

  private webScoketSubs?: Subscription;

  selectedEntity: EntityType = 'human-being';

  private humanObjectsSubject = new BehaviorSubject<HumanBeing[]>([]);
  humanObjects$ = this.humanObjectsSubject.asObservable();

  private carObjectsSubject = new BehaviorSubject<Car[]>([]);
  carObjects$ = this.carObjectsSubject.asObservable();

  private coordObjectsSubject = new BehaviorSubject<Coordinates[]>([]);
  coordObjects$ = this.coordObjectsSubject.asObservable();

  lastRequestObjects: any[] = [];

  filterForm: FormGroup;

  currentPage: number = 0;
  readonly PAGE_SIZE: number = 10;

  sortKey: string = "id";

  constructor(
    private objectsService: ObjectsService,
    private fb: FormBuilder,
    private webScoketService: WebScoketService
  ) {this.filterForm = this.fb.group({})}

  ngOnInit(): void {
    this.loadObjects();
    this.initForm();
    this.setupSseSubscription();
  }

  private initForm(): void {
    switch (this.selectedEntity) {
      case 'human-being':
        this.filterForm = this.fb.group({
          id: [''],
          name: [''],
          coordinates: [''],
          creationDate: [''],
          realHero: [''],
          hasToothpick: [''],
          car: [''],
          mood: [''],
          impactSpeed: [''],
          soundtrackName: [''],
          weaponType: [''],
          isUser: ['']
        });
        break;

      case 'car':
        this.filterForm = this.fb.group({
          id: [''],
          name: [''],
          cool: [''],
          isUser: ['']
        });
        break;

      case 'coordinates':
        this.filterForm = this.fb.group({
          id: [''],
          x: [''],
          y: [''],
          isUser: ['']
        });
    }
  }

  onEntityChange(entity: EntityType): void {
    if (this.selectedEntity !== entity) {
      this.selectedEntity = entity;
      this.currentPage = 0;
      this.resetObjects();
      this.initForm();
      this.loadObjects();

      if (this.webScoketSubs) {
        this.webScoketSubs.unsubscribe();
      }
      this.setupSseSubscription();
    }
  }

  setupSseSubscription(): void {
    this.webScoketSubs = this.webScoketService.subscribe(this.selectedEntity)
      .subscribe({
        next: (object: any) => {
  
          let currentList: any[] = [...this.lastRequestObjects];
  
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
              console.log(currentList);
              break;
          }
  
          this.updateTracks(currentList);
          this.updateObjects(currentList);
        },
        error: (error) => {
          console.log(error);
        }
      });
  }

  resetObjects(): void {
    this.humanObjectsSubject.next([]);
    this.carObjectsSubject.next([]);
    this.coordObjectsSubject.next([]);
  }

  loadObjects(): void {
    this.objectsService.getAllObjects(this.selectedEntity)
      .subscribe((objects: EntityDataMap[EntityType][]) => {
        this.updateObjects(objects);
      });
  }

  updateObjects(objects: EntityDataMap[EntityType][]): void {
      switch (this.selectedEntity) {
        case 'human-being':
          let humanList: HumanBeing[] = [];
          objects.forEach((value) => humanList.push(value as HumanBeing));
          this.updateTracks(humanList);
          this.humanObjectsSubject.next(humanList);
          break
        case 'car':
          let carList: Car[] = [];
          objects.forEach((value) => carList.push(value as Car));
          this.updateTracks(carList);
          this.carObjectsSubject.next(carList);
          break
        case 'coordinates':
          let coordinatesList: Coordinates[] = [];
          objects.forEach((value) => coordinatesList.push(value as Coordinates));
          this.updateTracks(coordinatesList);
          this.coordObjectsSubject.next(coordinatesList);
          break;
      }

      this.lastRequestObjects = objects;

      this.sortBy();
  }

  nextPage(): void {
    this.currentPage++;
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }

  hasMore(): boolean {
    return this.getCurrentObjects().length >= (this.currentPage + 1) * this.PAGE_SIZE;
  }

  getCurrentObjects(): any[] {
    switch (this.selectedEntity) {
      case 'human-being':
        return this.humanObjectsSubject.value;
      case 'car':
        return this.carObjectsSubject.value;
      case 'coordinates':
        return this.coordObjectsSubject.value;
      default:
        return [];
    }
  }

  trackBy(index: number, item: any): any {
    return item.id;
  }

  sortBy(): void {
    let sortedObjects: any[] = [];

    switch (this.selectedEntity) {
      case 'human-being':
        sortedObjects = [...this.humanObjectsSubject.value];
        sortedObjects.sort(this.compareValues(this.sortKey));
        this.humanObjectsSubject.next(sortedObjects);
        break;
      case 'car':
        sortedObjects = [...this.carObjectsSubject.value];
        sortedObjects.sort(this.compareValues(this.sortKey));
        this.carObjectsSubject.next(sortedObjects);
        break;
      case 'coordinates':
        sortedObjects = [...this.coordObjectsSubject.value];
        sortedObjects.sort(this.compareValues(this.sortKey));
        this.coordObjectsSubject.next(sortedObjects);
        break;
    }
  }

  private compareValues(key: string) {
    return (a: any, b: any) => {
      const aValue = a[key];
      const bValue = b[key];

      if (aValue === undefined || bValue === undefined) {
        return aValue === undefined ? 1 : -1;
      }

      if (typeof aValue === 'number' && typeof bValue === 'number') {
        return aValue - bValue;
      }

      if (typeof aValue === 'string' && typeof bValue === 'string') {
        return aValue.localeCompare(bValue);
      }

      return 0;
    };
  }

  formatDate(input: string): string {
    const months: { [key: string]: string } = {
        'янв.': '01', 'февр.': '02', 'март': '03', 'апр.': '04', 'май': '05', 'июнь': '06',
        'июль': '07', 'авг.': '08', 'сент.': '09', 'окт.': '10', 'нояб.': '11', 'дек.': '12'
    };

    const [day, month, year] = input.split(' ');
    const formattedMonth = months[month];

    return `${year}-${formattedMonth}-${day.padStart(2, '0')}`;
  }

  updateHumanFilter() {
    let humanFilter = this.filterForm.value as HumanBeing;
    
    let filteredList: HumanBeing[] = this.lastRequestObjects.filter(obj => {
      return (
        (humanFilter.id as unknown == "" || obj.id == humanFilter.id) &&
        (humanFilter.name == "" || obj.name.toLowerCase().includes(humanFilter.name.toLowerCase())) &&
        (humanFilter.coordinates as unknown == ""  || String(obj.coordinates) == humanFilter.coordinates) &&
        (humanFilter.creationDate == "" || this.formatDate(obj.creationDate) == humanFilter.creationDate) &&
        (humanFilter.realHero as unknown  == "" || String(obj.realHero) == String(humanFilter.realHero)) &&
        (humanFilter.hasToothpick as unknown == "" || String(obj.hasToothpick) == String(humanFilter.hasToothpick)) &&
        (humanFilter.car as unknown == ""  || (obj.car != null && String(obj.car) == humanFilter.car)) &&
        (humanFilter.mood as string == "" || obj.mood.toLowerCase().includes(humanFilter.mood.toLowerCase())) &&
        (humanFilter.impactSpeed as unknown == ""  || obj.impactSpeed == humanFilter.impactSpeed) &&
        (humanFilter.soundtrackName == '' || obj.soundtrackName.toLowerCase().includes(humanFilter.soundtrackName.toLowerCase())) &&
        (humanFilter.weaponType as unknown == ""  || obj.weaponType.toLowerCase().includes(humanFilter.weaponType.toLowerCase())) &&
        (humanFilter.isUser == '' || obj.isUser.toLowerCase().includes(humanFilter.isUser.toLowerCase()))
      );
    });

    this.updateTracks(filteredList);
    this.humanObjectsSubject.next(filteredList);
  }

  updateCarFilter() {
    let carFilter = this.filterForm.value as Car;

    let filteredList: Car[] = this.lastRequestObjects.filter(obj => {
      return (
        (carFilter.id as unknown == "" || obj.id == carFilter.id) &&
        (carFilter.name == "" || obj.name.toLowerCase().includes(carFilter.name.toLowerCase())) &&
        (carFilter.cool as unknown == "" || String(obj.cool) == String(carFilter.cool)) &&
        (carFilter.isUser == '' || obj.isUser.toLowerCase().includes(carFilter.isUser.toLowerCase()))
      )
    })
    this.updateTracks(filteredList);
    this.carObjectsSubject.next(filteredList);
  }

  updateCoordinatesFilter() {
    let coordsFilter = this.filterForm.value as Coordinates;

    let filteredList: Coordinates[] = this.lastRequestObjects.filter(obj => {
      return (
        (coordsFilter.id as unknown == "" || obj.id == coordsFilter.id) &&
        (coordsFilter.x as unknown == "" || String(obj.x) == String(coordsFilter.x)) &&
        (coordsFilter.y as unknown == "" || String(obj.y) == String(coordsFilter.y)) &&
        (coordsFilter.isUser == '' || obj.isUser.toLowerCase().includes(coordsFilter.isUser.toLowerCase()))
      )
    })
    this.updateTracks(filteredList);
    this.coordObjectsSubject.next(filteredList);
  }

  updateTracks(object: HumanBeing[] | Car[] | Coordinates[]) {
    let track: number = 0;
    object.forEach(object => {
      object.track = track++;
    })
  }
}
