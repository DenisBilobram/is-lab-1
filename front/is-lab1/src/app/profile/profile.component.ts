import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { error } from 'node:console';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [RouterLink, NgClass],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {

  username: string;
  email: string;
  isAdmin: boolean;
  requestInfo?: string;
  requestSuccses?: boolean;

  constructor(private authServiec: AuthService) {
    this.email = localStorage['email'];
    this.username = localStorage['username'];
    this.isAdmin = Boolean(localStorage['isAdmin']);
  }
  ngOnInit(): void {
    this.authServiec.getRoles().subscribe({
      next: (roles: string[]) => {
        if (roles.indexOf("ADMIN") != -1) {
          this.isAdmin = true;
        }
      }
    })
  }

  onAdminRoots() {
    this.authServiec.adminRootsRequest().subscribe({
      next: (response) => {
        this.requestInfo = "Created request for roots.";
        this.requestSuccses = true;
      },
      error: (error) => {
        this.requestInfo = "Request has been already created.";
        this.requestSuccses = false;
      }
    })
  }

}
