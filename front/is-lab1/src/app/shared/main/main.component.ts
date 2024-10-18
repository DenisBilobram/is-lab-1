import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { RouterOutlet } from '@angular/router';
import { ObjectsManagerComponent } from '../../objects/objects-manager/objects-manager.component';
import { ProfileComponent } from '../../profile/profile.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [HeaderComponent, RouterOutlet, ObjectsManagerComponent, ProfileComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

}
