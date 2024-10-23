import { Component, OnInit } from '@angular/core';
import { EntityType } from '../objects-manager/objects-manager.component';
import { ObjectsService } from '../objects.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, RouterLink, Router } from '@angular/router';
import { error } from 'console';
import { Car } from '../models/car.model';
import { Coordinates } from '../models/coordinates.model';

@Component({
  selector: 'app-objects-creator',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './objects-creator.component.html',
  styleUrl: './objects-creator.component.scss'
})
export class ObjectsCreatorComponent {

  entityType: EntityType | null = null;

  objectForm?: FormGroup;

  errorMessage: string = '';

  carsList: Car[] = [];

  coordinatesList: Coordinates[] = [];

  objectId?: number | null = null;

  loadedObject: any;

  isEditMode?: boolean;

  constructor(private objectsService: ObjectsService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router) {
      this.route.paramMap.subscribe((params: ParamMap) => {
      const entityTypeParam = params.get('entityType');
      this.entityType = entityTypeParam as EntityType;

      const idParam = params.get('id');
      if (!isNaN(Number(idParam))) {
        this.objectId = Number(idParam);
      }
      this.isEditMode = !!this.objectId;
      this.initForm();

      if (this.isEditMode && this.objectId) {
        this.loadObject();
      }
    });

    this.loadObjects();
  }

  private initForm(): void {
    switch (this.entityType) {
      case 'human-being':
        this.loadObjects();
        this.objectForm = this.fb.group({
          name: ['', Validators.required],
          coordinates: ['', Validators.required],
          realHero: [false, Validators.required],
          hasToothpick: [false, Validators.required],
          car: [false],
          mood: ['', Validators.required],
          impactSpeed: [null, Validators.required],
          soundtrackName: ['', Validators.required],
          weaponType: ['', Validators.required]
        });
        break;

      case 'car':
        this.objectForm = this.fb.group({
          name: ['', Validators.required],
          cool: [false, Validators.required]
        });
        break;

      case 'coordinates':
        this.objectForm = this.fb.group({
          x: [null, [Validators.required, Validators.min(-419)]],
          y: [null, [Validators.required, Validators.min(-453)]]
        });
        break;

      default:
        console.error('Unknown entity type');
        console.log(this.entityType);
        break;
    }
  }

  onSubmit() {
    if (this.objectForm?.invalid || !this.entityType) {
      this.errorMessage = "Invalid form.";
      return;
    }
    if (this.isEditMode && this.objectId) {
      this.objectsService.updateObject(this.entityType, this.objectId, this.objectForm?.value).subscribe(
        response => {
          console.log('Object updated', response);
        },
      );
    } else {
      this.objectsService.postObject(this.entityType, this.objectForm?.value).subscribe(
        response => {
          console.log('Object created', response);
        },
      );
    }

  }

  loadObject() {
    if (this.entityType != null && this.objectId != null) {
      this.objectsService.getObject(this.entityType, this.objectId).subscribe(object => {
        this.objectForm?.patchValue(object);
        this.loadedObject = object;
        console.log(object);
      })
    }
  }

  loadObjects(){
    this.objectsService.getAllObjects('car').subscribe(objects => {
      this.carsList = objects as Car[];
    })
    this.objectsService.getAllObjects('coordinates').subscribe(objects => {
      this.coordinatesList = objects as Coordinates[];
    })
  }

  onDeleteObject() {
    if (this.entityType != null && this.objectId != null) {
      this.objectsService.deleteObject(this.entityType, this.objectId).subscribe(response => {
        console.log("Deleted object.")
        this.router.navigate(["/objects"]);
      })
    }
  }
  
}
