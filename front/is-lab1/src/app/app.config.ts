import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './auth/auth.Interceptor';
import { jwtInterceptor } from './auth/jwt.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideHttpClient(withInterceptors([authInterceptor, jwtInterceptor]), withFetch()), provideClientHydration()]
};

export const URL = 'http://localhost:8080'

export const API_URLS = {
  AUTH: URL + '/api/auth',
  OBJECTS: URL + '/api/objects',
  ROOTS: URL + '/api/roots',
  WEBSOCKET: URL + '/ws',
};
