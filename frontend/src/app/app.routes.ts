import { Routes } from '@angular/router';
import { OrganisationComponent } from './components/organisation/organisation.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { AuthGuard } from './core/guards/auth.guard';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
    { path: '', redirectTo: 'welcome', pathMatch: 'full' },
    {
        path: 'welcome',
        component: WelcomeComponent,
        canActivate: [],
    },
    {
        path: 'organisations/:id',
        component: OrganisationComponent,
        canActivate: [AuthGuard],
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [],
    },
    {
        path: 'register',
        component: RegisterComponent,
        canActivate: [],
    },
    {
        path: '**',
        pathMatch: 'full',
        redirectTo: 'welcome',
    },
];
