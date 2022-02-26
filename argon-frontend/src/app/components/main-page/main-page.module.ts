import { MainPageComponent } from './main-page.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TopBarModule } from '../top-bar/top-bar.module';
import { MatSidenavModule } from '@angular/material/sidenav';
import { OrganizationsModule } from '../organizations/organizations.module';
import { SharedModule } from 'src/app/shared.module';

@NgModule({
  declarations: [ MainPageComponent ],
  imports: [
    CommonModule,
    SharedModule,
    TopBarModule,
    MatSidenavModule,
    OrganizationsModule
  ],
  exports: [],
  providers: [],
})
export class MainPageModule {}
