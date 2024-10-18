import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './auth/auth.Interceptor';
import { jwtInterceptor } from './auth/jwt.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideHttpClient(withInterceptors([authInterceptor, jwtInterceptor])),provideClientHydration()]
};

export const API_URLS = {
  AUTH: 'http://localhost:8080/api/auth',
  USERS: 'http://localhost:8080/api/users',
  PRODUCTS: 'http://localhost:8080/api/products'
};
