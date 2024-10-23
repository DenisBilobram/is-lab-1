import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const logoutGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  if (typeof window !== 'undefined' && window.localStorage) {
    localStorage.removeItem('authToken');
    localStorage.removeItem('email');
    localStorage.removeItem('username');
  }
  

  router.navigate(['/auth/login']);
  
  return false;
};
