import { OrganizationsComponent } from './organizations.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { OrganizationComponent } from './organization/organization.component';
import { OrganizationDialogComponent } from './organization-dialog/organization-dialog.component';
import { SharedModule } from 'src/app/shared.module';
import { OrganizationViewModule } from '../organization-view/organization-view.module';

@NgModule({
  declarations: [
    OrganizationsComponent,
    OrganizationComponent,
    OrganizationDialogComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    MatCardModule,
    OrganizationViewModule,
  ],
  exports: [
    OrganizationsComponent
  ],
  providers: [],
})
export class OrganizationsModule { }
