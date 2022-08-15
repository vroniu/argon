import { OrganizationViewComponent } from './components/organization-view/organization-view.component';
import { AuthGuard } from './auth/guards/auth.guard';
import { MainPageComponent } from './components/main-page/main-page.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrganizationsComponent } from './components/organizations/organizations.component';

export const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'main', component: MainPageComponent, canActivate: [AuthGuard], children: [
    { path: 'organizations/:id', component: OrganizationViewComponent},
    { path: 'organizations', component: OrganizationsComponent },
    { path: '**', redirectTo: 'organizations'},
  ] },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
