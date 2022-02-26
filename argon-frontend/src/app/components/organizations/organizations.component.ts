import { OrganizationDialogComponent } from './organization-dialog/organization-dialog.component';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Organization } from 'src/app/models/organization.model';
import { OrganizationService } from 'src/app/services/organization.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'arg-organizations',
  templateUrl: './organizations.component.html',
  styleUrls: ['./organizations.component.css']
})
export class OrganizationsComponent implements OnInit {
  ownedOrganizations: Organization[] = [];
  joinedOrganizations: Organization[] = [];

  constructor(
    private organizationService: OrganizationService,
    private dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.loadOwnedOrganizations();
    this.loadJoinedOrganizations();
  }

  loadOwnedOrganizations() {
    this.organizationService.getOrganizationsOwnedByUser().subscribe(organizations => {
      const addOrganizationCard = new Organization();
      addOrganizationCard.addOrganizationCard = true;
      addOrganizationCard.name = 'Create organization';
      this.ownedOrganizations = [...organizations, addOrganizationCard];
    });
  }

  loadJoinedOrganizations() {
    this.organizationService.getOrganizationsJoinedByUser().subscribe(organizations => {
      this.joinedOrganizations = organizations;
    });
  }

  onOrganizationClicked(organization: Organization, owner: boolean) {
    if (organization.addOrganizationCard) {
      const dialogRef = this.dialog.open(OrganizationDialogComponent);
      dialogRef.afterClosed().subscribe(data => {
        if (data.save) {
          this.organizationService.createOrganization(data.organization).subscribe(() => this.loadOwnedOrganizations());
        }
      });
    } else {
      this.router.navigate(
        [`${organization.id}`],
        {
          relativeTo: this.route,
          queryParams: { owner: owner }
        }
      );
    }
  }

}
