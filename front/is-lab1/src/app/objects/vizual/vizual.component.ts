import { NgStyle } from '@angular/common';
import { AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { HumanBeing } from '../models/human-being.model';
import { Coordinates } from '../models/coordinates.model';
import { Mood } from '../models/mood.enum';
import { WeaponType } from '../models/weapon-type.enum';
import { ObjectsService } from '../objects.service';
import { WebScoketService } from '../web-scoket.service';

interface Point {
  x: number;
  y: number;
}

@Component({
  selector: 'app-vizual',
  standalone: true,
  imports: [NgStyle],
  templateUrl: './vizual.component.html',
  styleUrl: './vizual.component.scss'
})
export class VizualComponent implements AfterViewInit {
  @ViewChild('canvas', { static: false }) canvasRef!: ElementRef<HTMLCanvasElement>;
  private ctx!: CanvasRenderingContext2D;

  isDragging = false;
  lastMouseX = 0;
  lastMouseY = 0;
  offsetX = 0;
  offsetY = 0;

  objectsList: Array<{human: HumanBeing, coordinates: Coordinates}> = [];
  humansList: HumanBeing[] = [];
  coordinatesList: Coordinates[] = [];

  chosenObjects: Array<{human: HumanBeing, coordinates: Coordinates}> = [];

  constructor(private objectsService: ObjectsService,private webScoketService: WebScoketService) {}

  ngAfterViewInit(): void {
    this.objectsService.getAllObjects('human-being').subscribe({
      next: (humans: HumanBeing[]) => {
        this.humansList = humans;
        this.objectsService.getAllObjects('coordinates').subscribe({
          next: (coordinates: Coordinates[]) => {
            this.coordinatesList = coordinates;
            this.updateHumansWithCoordinates();
            const canvas = this.canvasRef.nativeElement;
            this.ctx = canvas.getContext('2d')!;
            this.drawGrid();
          }
        })
      }
    })
    this.webScoketService.subscribe('human-being').subscribe({
      next: (human: HumanBeing) => { 
        this.objectsService.getAllObjects('coordinates').subscribe({
          next: (coordinates: Coordinates[]) => {
            this.coordinatesList = coordinates;
            this.humansList = this.objectsService.perfromeEvent(this.humansList, human);
            this.updateHumansWithCoordinates();
            this.drawGrid();
          }
        })
      }
    })
  }

  updateHumansWithCoordinates() {
    this.objectsList = [];
    this.humansList.forEach(human => {
      for (let i = 0; i < this.coordinatesList.length; i++) {
        if (this.coordinatesList[i].id == human.coordinates) {
          this.objectsList.push({human: human, coordinates: this.coordinatesList[i]});
          break;
        }
      }
    })
  }

  drawGrid(): void {
    const canvas = this.canvasRef.nativeElement;
    const ctx = this.ctx;
    const width = canvas.width;
    const height = canvas.height;
    const gridSize = 40;
  
    ctx.clearRect(0, 0, width, height);

    const originX = width / 2 + this.offsetX;
    const originY = height / 2 + this.offsetY;
  
    ctx.strokeStyle = '#ccc';
    ctx.lineWidth = 1;
  
    for (let x = originX; x <= width; x += gridSize) {
      ctx.beginPath();
      ctx.moveTo(x, 0);
      ctx.lineTo(x, height);
      ctx.stroke();
    }
    for (let x = originX - gridSize; x >= 0; x -= gridSize) {
      ctx.beginPath();
      ctx.moveTo(x, 0);
      ctx.lineTo(x, height);
      ctx.stroke();
    }

    for (let y = originY; y <= height; y += gridSize) {
      ctx.beginPath();
      ctx.moveTo(0, y);
      ctx.lineTo(width, y);
      ctx.stroke();
    }
    for (let y = originY - gridSize; y >= 0; y -= gridSize) {
      ctx.beginPath();
      ctx.moveTo(0, y);
      ctx.lineTo(width, y);
      ctx.stroke();
    }
    
    ctx.closePath()

    ctx.strokeStyle = 'white';
    ctx.lineWidth = 1;
  
    ctx.beginPath();
    ctx.moveTo(originX, 0);
    ctx.lineTo(originX, height);
    ctx.stroke();
  
    ctx.beginPath();
    ctx.moveTo(0, originY);
    ctx.lineTo(width, originY);
    ctx.stroke();

    ctx.lineWidth = 1;
    for (let x = originX; x <= width; x += gridSize) {
      ctx.beginPath();
      ctx.moveTo(x, originY - 5);
      ctx.lineTo(x, originY + 5);
      ctx.stroke();
    }
    for (let x = originX - gridSize; x >= 0; x -= gridSize) {
      ctx.beginPath();
      ctx.moveTo(x, originY - 5);
      ctx.lineTo(x, originY + 5);
      ctx.stroke();
    }
    for (let y = originY; y <= height; y += gridSize) {
      ctx.beginPath();
      ctx.moveTo(originX - 5, y);
      ctx.lineTo(originX + 5, y);
      ctx.stroke();
    }
    for (let y = originY - gridSize; y >= 0; y -= gridSize) {
      ctx.beginPath();
      ctx.moveTo(originX - 5, y);
      ctx.lineTo(originX + 5, y);
      ctx.stroke();
    }

    this.objectsList.forEach(obj => {
      const humanX = originX + obj.coordinates.x;
      const humanY = originY - obj.coordinates.y;
    
      ctx.beginPath();
      ctx.arc(humanX, humanY - 10, 5, 0, 2 * Math.PI);
      ctx.strokeStyle = this.stringToColor(obj.human.isUser);
      ctx.lineWidth = 2;
      ctx.stroke();
    

      ctx.beginPath();
      ctx.moveTo(humanX, humanY - 5); 
      ctx.lineTo(humanX, humanY + 10);
      ctx.stroke();

      ctx.beginPath();
      ctx.moveTo(humanX, humanY + 10); 
      ctx.lineTo(humanX - 5, humanY + 20);
      ctx.stroke();

      ctx.beginPath();
      ctx.moveTo(humanX, humanY + 10); 
      ctx.lineTo(humanX + 5, humanY + 20);
      ctx.stroke();

      ctx.beginPath();
      ctx.moveTo(humanX, humanY); 
      ctx.lineTo(humanX + 5, humanY + 2);
      ctx.stroke();

      ctx.beginPath();
      ctx.moveTo(humanX, humanY); 
      ctx.lineTo(humanX - 5, humanY + 2);
      ctx.stroke();
 
      ctx.fillStyle = 'white';
      ctx.font = '12px Arial';
      ctx.fillText(obj.human.name, humanX + 15, humanY);
    });

    ctx.fillText(`X: ${-this.offsetX}`, width - 100, 30);
    ctx.fillText(`Y: ${this.offsetY}`, width - 50, 30);

  }

  onMouseDown(event: MouseEvent): void {
    this.isDragging = true;
    this.lastMouseX = event.clientX;
    this.lastMouseY = event.clientY;
  }

  onMouseUp(event: MouseEvent): void {
    this.isDragging = false;
  }

  onMouseMove(event: MouseEvent): void {
    if (this.isDragging) {
      const deltaX = event.clientX - this.lastMouseX;
      const deltaY = event.clientY - this.lastMouseY;
      this.lastMouseX = event.clientX;
      this.lastMouseY = event.clientY;

      this.offsetX += deltaX;
      this.offsetY += deltaY;

      this.drawGrid();
    }
  }

  @HostListener('click', ['$event'])
  onCanvasClick(event: MouseEvent): void {

    const rect = this.canvasRef.nativeElement.getBoundingClientRect();
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;

    let objects: any[] = [];

    this.objectsList.forEach(obj => {
      const humanX = this.canvasRef.nativeElement.width / 2 + this.offsetX + obj.coordinates.x;
      const humanY = this.canvasRef.nativeElement.height / 2 + this.offsetY - obj.coordinates.y + 10;
      if (Math.abs(clickX - humanX) <= 5 && Math.abs(clickY - humanY) <= 20) {
        objects.push(obj);
      }
    });

    if (objects.length != 0) {
      this.chosenObjects = [...objects];
    }
  }

  stringToColor(str: string) {
    let hash = 0;
    
    for (let i = 0; i < str.length; i++) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    
    let color = '#';
    for (let i = 0; i < 3; i++) {
      const value = (hash >> (i * 8)) & 0xFF;
      color += ('00' + value.toString(16)).slice(-2);
    }
    
    return color;
  }
}
