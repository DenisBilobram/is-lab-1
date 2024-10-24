import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';
import { error } from 'node:console';

@Component({
  selector: 'app-auth-manager',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './auth-manager.component.html',
  styleUrl: './auth-manager.component.scss'
})
export class AuthManagerComponent {
  authform: FormGroup;
  registrForm: FormGroup;
  
  isRegister: boolean = false;

  errorMessage: string = "";

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {

    this.authform = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(6)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    this.registrForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    })
  }

  onLoginSubmit() {
    if (this.authform.valid) {
      const credentials = this.authform.value;
      this.authService.login(credentials).subscribe({
        next: () => {
          this.router.navigate(['/objects']);
          this.errorMessage = "";
        },
        error: err => {
          this.errorMessage = err.error.message;
          console.log(err)
        }
      });
    } else {
      console.log('authform is invalid');
    }
  }

  onRegisterSubmit() {
    if (this.registrForm.valid) {
      const credentials = this.registrForm.value;
      this.authService.register(credentials).subscribe({
        next: () => {
          this.router.navigate(['/objects']);
          this.errorMessage = "";
        },
        error: err => {
          this.errorMessage = err.error.message;
        }
      });
    } else {
      console.log('registrForm is invalid');
    }
  }

  
}
