import { MatCardModule } from '@angular/material/card';
import { SharedModule } from 'src/app/shared.module';
import { OrganizationViewComponent } from './organization-view.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubmitWorktimeComponent } from './submit-worktime/submit-worktime.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSliderModule } from '@angular/material/slider';
import { WorktimeTableComponent } from './worktime-table/worktime-table.component';
import { EditWorktimeDialogComponent } from './edit-worktime-dialog/edit-worktime-dialog.component';
import { WorktimeFormComponent } from './worktime-form/worktime-form.component';
import { WorktimeArchiveComponent } from './worktime-archive/worktime-archive.component';
import { ManageWorktimesComponent } from './manage-worktimes/manage-worktimes.component';
import { ManageProjectsComponent } from './manage-projects/manage-projects.component';
import { ManageOrganizationComponent } from './manage-organization/manage-organization.component';
import {MatTreeModule} from '@angular/material/tree';

@NgModule({
  declarations: [
    OrganizationViewComponent,
    SubmitWorktimeComponent,
    WorktimeTableComponent,
    EditWorktimeDialogComponent,
    WorktimeFormComponent,
    WorktimeArchiveComponent,
    ManageWorktimesComponent,
    ManageProjectsComponent,
    ManageOrganizationComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    MatCardModule,
    MatDatepickerModule,
    MatSliderModule,
    MatTreeModule
  ],
  exports: [
    OrganizationViewComponent
  ],
  providers: [],
})
export class OrganizationViewModule { }