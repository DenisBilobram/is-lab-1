import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { error } from 'node:console';
import { NgClass } from '@angular/common';
import { RootsService } from './roots.service';

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

  rootsRequests: any[] = [];

  constructor(private rootsService: RootsService) {
    this.email = localStorage['email'];
    this.username = localStorage['username'];
    this.isAdmin = Boolean(localStorage['isAdmin']);

  }
  ngOnInit(): void {
    this.rootsService.getRoles().subscribe({
      next: (roles) => {
        if (Boolean(localStorage['isAdmin'])) {
          this.isAdmin = true;
          this.getRootsRequests();
        }
      }
    });
  }

  onAdminRoots() {
    this.rootsService.adminRootsRequest().subscribe({
      next: (response) => {
        this.requestInfo = "Created request for roots.";
        this.requestSuccses = true;
      },
      error: (error) => {
        this.requestInfo = error.error.message;
        this.requestSuccses = false;
      }
    })
  }

  getRootsRequests() {
    this.rootsService.getRootsRequests().subscribe({
      next: (requests) => {
        this.rootsRequests = [...requests];
        console.log(this.rootsRequests);
      }
    })
  }

  requestAprove(id: number) {
    this.rootsService.aproveRootsRequest(id).subscribe({
      next: (response) => {
        this.rootsRequests = [...(this.rootsRequests.filter(el => el.id != id))]
      }
    })
  }

  requestDecline(id: number) {
    this.rootsService.declineRootsRequest(id).subscribe({
      next: (response) => {
        this.rootsRequests = [...(this.rootsRequests.filter(el => el.id != id))]
      }
    })
  }

}
