import { Organization } from './../../../models/organization.model';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'arg-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.css']
})
export class OrganizationComponent implements OnInit {
  @Input() organization: Organization = null;
  @Output() onOrganizationClicked = new EventEmitter<any>();

  constructor() { }

  ngOnInit(): void {
  }

  onOrganizationCardClicked() {
    this.onOrganizationClicked.emit();
  }

}
