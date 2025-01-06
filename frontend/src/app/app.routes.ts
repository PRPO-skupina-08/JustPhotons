import { Routes } from '@angular/router';
import { OrganisationComponent } from './components/organisation/organisation.component';
import { WelcomeComponent } from './components/welcome/welcome.component';

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
        canActivate: [],
    },
    {
        path: '**',
        pathMatch: 'full',
        redirectTo: 'welcome',
    },
];
