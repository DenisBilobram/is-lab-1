import { NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { error } from 'console';

@Component({
  selector: 'app-password-reset',
  standalone: true,
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './password-reset.component.html',
  styleUrl: './password-reset.component.scss'
})
export class PasswordResetComponent {

  emailForm: FormGroup;
  passwordForm: FormGroup;
  resultMessage: string = '';
  token: string | null;
  resetSended?: boolean;

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private authService: AuthService, private router: Router) {
    this.emailForm = fb.group({
      email: ['', Validators.required],
    })
    this.passwordForm = fb.group({
      password: ['', Validators.required, Validators.minLength(6)],
    })

    this.token = this.route.snapshot.queryParamMap.get('token');
    console.log(this.token)
  }

  forgotPassword() {
    this.authService.forgotPassword(this.emailForm.value).subscribe({
      next: (response) => {
        this.resetSended = true;
        this.resultMessage = "Sended message with password reset at the specified address."
      }, error: (error) => {
        this.resetSended = false;
        this.resultMessage = error.error.message;
      }
    })
  }

  resetPassword() {
    if (!this.token) return;
    this.authService.resetPassword({token: this.token, newPassword: this.passwordForm.get('password')?.value}).subscribe({
      next: (response) => {
        this.resultMessage = "Updated password.";
        this.router.navigate(['/auth/login'])
      }, error: (error) => {
        this.resultMessage = error.error.message; 
      }
    })
  }
}
