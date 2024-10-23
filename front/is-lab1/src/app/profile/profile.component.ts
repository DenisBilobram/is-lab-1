import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {

  username: string;
  email: string;
  isAdmin: boolean;

  constructor(private authServiec: AuthService) {
    this.email = localStorage['email'];
    this.username = localStorage['username'];
    this.isAdmin = Boolean(localStorage['isAdmin']);
  }

  onAdminRoots() {

  }

}
