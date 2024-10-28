import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { Car } from '../../models/car.model';
import { HumanBeing } from '../../models/human-being.model';
import { Coordinates } from '../../models/coordinates.model';
import { EntityType } from '../objects-manager.component';
import { RouterLink } from '@angular/router';
import { NgClass, NgStyle } from '@angular/common';

@Component({
  selector: 'app-object-line',
  standalone: true,
  imports: [RouterLink, NgClass],
  templateUrl: './object-line.component.html',
  styleUrl: './object-line.component.scss'
})
export class ObjectLineComponent implements OnInit {

  @Input() object: Car | HumanBeing | Coordinates | null = null;
  @Input() entityType: EntityType | null = null;

  humanBeing?: HumanBeing;
  car?: Car;
  coordinates?: Coordinates;
  isAllowed?: any;


  ngOnInit(): void {
    if (this.object) {
      
      switch (this.entityType) {
        case 'human-being':
          this.humanBeing = (this.object as HumanBeing);
          break;
        case 'car':
          this.car = (this.object as Car);
          break;
        case 'coordinates':
          this.coordinates = (this.object as Coordinates);
      }
    }

    this.isAllowed = (localStorage["username"] == this.object?.isUser || (localStorage["isAdmin"] as boolean && this.object?.adminsCanEdit));
  }

}
