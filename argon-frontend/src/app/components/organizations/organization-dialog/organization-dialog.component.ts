import { FormGroup } from '@angular/forms';
import { Organization } from 'src/app/models/organization.model';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as _ from 'lodash';

export interface OrganizationDialogData {
  editMode?: boolean;
  organization?: Organization;
}

@Component({
  selector: 'arg-organization-dialog',
  templateUrl: './organization-dialog.component.html',
  styleUrls: ['./organization-dialog.component.css']
})
export class OrganizationDialogComponent implements OnInit {
  @ViewChild('organizationForm') organizationForm: FormGroup;
  organization: Organization = new Organization();
  editMode: boolean;

  constructor(private dialogRef: MatDialogRef<OrganizationDialogComponent>, @Inject(MAT_DIALOG_DATA) private data: OrganizationDialogData) { }

  ngOnInit(): void {
    this.editMode = this.data.editMode;
    if (this.editMode) {
      this.organization = _.cloneDeep(this.data.organization);
    }
  }

  onClose(save: boolean) {
    if (save) {
      if (this.organizationForm.valid) {
        this.dialogRef.close({
          save: true,
          organization: this.organization
        });
      }
    } else {
      this.dialogRef.close({ save: false });
    }
  }

}
