import { Component } from '@angular/core';
import { Operation } from './operation';
import { ObjectsService } from '../objects.service';
import { Form, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, NgClass } from '@angular/common';
import { WeaponType } from '../models/weapon-type.enum';
import { response } from 'express';

@Component({
  selector: 'app-operations',
  standalone: true,
  imports: [ReactiveFormsModule, NgClass, CommonModule],
  templateUrl: './operations.component.html',
  styleUrl: './operations.component.scss'
})
export class OperationsComponent {
  operationsList: Operation[];

  selectedOperation?: Operation;

  formInputs: FormGroup;

  fieldsMap: any;

  result: string = "";

  constructor(private objectsSerive: ObjectsService, private fb: FormBuilder) {
    this.operationsList = [
      {
        name: 'countImpactSpeed',
        description: 'Вернуть количество объектов, значение поля impactSpeed которых равно заданному.',
        inputs: [{ label: 'Impact Speed', name: 'impactSpeed', type: 'number', options: [] }],
        execute: this.countImpactSpeed.bind(this),
      },
      {
        name: 'filterName',
        description: 'Вернуть массив объектов, значение поля name которых содержит заданную подстроку.',
        inputs: [{ label: 'Name Substring', name: 'substring', type: 'text', options: [] }],
        execute: this.findByNameContaining.bind(this),
      },
      {
        name: 'filterWeaponType',
        description: 'Вернуть массив объектов, значение поля weaponType которых больше заданного.',
        inputs: [{ label: 'Weapon Type', name: 'weaponType', type: 'select', options: [
          WeaponType.AXE, WeaponType.HAMMER, WeaponType.KNIFE, WeaponType.PISTOL, WeaponType.SHOTGUN
        ]}],
        execute: this.findByWeaponTypeGreaterThan.bind(this),
      },
      {
        name: 'deleteNoToothpick',
        description: 'Удалить всех героев без зубочисток.',
        inputs: [],
        execute: this.deleteAllWithoutToothpick.bind(this),
      },
      {
        name: 'setMaxSadMood',
        description: 'Поменять всем героям настроение на максимально печальное.',
        inputs: [],
        execute: this.updateMoodForAll.bind(this),
      },
    ]

    this.formInputs = fb.group({});

  }

  setOperation(operation: Operation) {
    this.selectedOperation = operation;
    this.result = "";

    const group: { [key: string]: FormControl } = {};
    operation.inputs.forEach(input => {
      if (input.type === 'select') {
        group[input.name] = new FormControl(input.options[0], Validators.required);
      } else {
        group[input.name] = new FormControl('', Validators.required);
      }
    });
    this.formInputs = this.fb.group(group);
  }

  countImpactSpeed() {
    if (this.selectedOperation == null) return;
  
    const impactSpeed = this.formInputs.get(this.selectedOperation.inputs[0].name)?.value as number;
    this.objectsSerive.countImpactSpeed(impactSpeed).subscribe({
      next: (count) => {
        this.result = `Count: ${count}`;
      }
    });
  }

  findByNameContaining() {
    if (this.selectedOperation == null) return;
  
    const substringValue = this.formInputs.get(this.selectedOperation.inputs[0].name)?.value as string;
    this.objectsSerive.findByNameContaining(substringValue).subscribe({
      next: (array: any[]) => {
        if (array.length != 0) {
          array = this.clearifyObjects(array);
          this.result = 'Array:<br>[<br>' + array.join('<br>') + '<br>]';
        } else {
          this.result = 'Array: []';
        }
        
      }
    });
  }

  findByWeaponTypeGreaterThan() {
    if (this.selectedOperation == null) return;
  
    const weapon = this.formInputs.get(this.selectedOperation.inputs[0].name)?.value;
    this.objectsSerive.findByWeaponTypeGreaterThan(weapon).subscribe({
      next: (array: any[]) => {
        if (array.length != 0) {
          array = this.clearifyObjects(array);
          this.result = 'Array:<br>[<br>' + array.join('<br>') + '<br>]';
        } else {
          this.result = 'Array: []';
        }
      }
    })
  }

  deleteAllWithoutToothpick() {
    if (this.selectedOperation == null) return;

    this.objectsSerive.deleteAllWithoutToothpick().subscribe({
      next: (response) => {
        this.result = 'Deleted.';
      }
    })
  }

  updateMoodForAll() {
    if (this.selectedOperation == null) return;

    this.objectsSerive.updateMoodForAll().subscribe({
      next: (response) => {
        this.result = 'Updated.';
      }
    })
  }

  clearifyObjects(array: any[]): any[] {
    return array.map((el) => {
      delete el.objectEvents;
      delete el.type;
      delete el.carName;
      delete el.carCool;
      delete el.coordinatesX;
      delete el.coordinatesY;
      return JSON.stringify(el, null, 2);
    });
  }

  isValid(): boolean {
  if (!this.selectedOperation) return false;
  if (this.selectedOperation.inputs.length === 0) return true;

  return this.selectedOperation.inputs.every(input => {
    const control = this.formInputs.get(input.name);
    return control ? control.valid : false;
  });
}

  
}