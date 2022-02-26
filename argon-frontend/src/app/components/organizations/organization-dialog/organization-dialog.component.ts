import { FormGroup } from '@angular/forms';
import { Organization } from 'src/app/models/organization.model';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'arg-organization-dialog',
  templateUrl: './organization-dialog.component.html',
  styleUrls: ['./organization-dialog.component.css']
})
export class OrganizationDialogComponent implements OnInit {
  @ViewChild('organizationForm') organizationForm: FormGroup;
  organization: Organization = new Organization();

  constructor(private dialogRef: MatDialogRef<OrganizationDialogComponent>) { }

  ngOnInit(): void {

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
