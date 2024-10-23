import { Routes } from '@angular/router';
import { ObjectsManagerComponent } from './objects/objects-manager/objects-manager.component';
import { AuthManagerComponent } from './auth/auth-manager/auth-manager.component';
import { PasswordResetComponent } from './auth/password-reset/password-reset.component';
import { ProfileComponent } from './profile/profile.component';
import { EmptyComponent } from './auth/empty.component';
import { logoutGuard } from './auth/logout.guard';
import { AppComponent } from './app.component';
import { MainComponent } from './shared/main/main.component';
import { ObjectsCreatorComponent } from './objects/objects-creator/objects-creator.component';

export const routes: Routes = [
    {
        path: '',
        component: MainComponent,
        children: [
            {
                path: 'objects',
                title: 'Main Objects Page',
                component: ObjectsManagerComponent,
                children: []
            },
            {
                path: 'profile',
                title: 'Profile page',
                component: ProfileComponent,
            },
        ]
    },
    {
        path: 'objects/create/:entityType',
        title: 'Object Create Page',
        component: ObjectsCreatorComponent
    },
    {
        path: 'objects/edit/:entityType/:id',
        title: 'Object Edit Page',
        component: ObjectsCreatorComponent
    },
    {
        path: 'auth/login',
        title: 'Login Page',
        component: AuthManagerComponent
    },
    {
        path: 'auth/logout',
        title: 'Logout Page',
        component: EmptyComponent,
        canActivate: [logoutGuard]
    },
    {
        path: 'auth/login/password',
        title: 'Password Reset Page',
        component: PasswordResetComponent
    },
];
