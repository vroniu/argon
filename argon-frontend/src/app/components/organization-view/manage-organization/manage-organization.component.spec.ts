import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageOrganizationComponent } from './manage-organization.component';

describe('ManageOrganizationComponent', () => {
  let component: ManageOrganizationComponent;
  let fixture: ComponentFixture<ManageOrganizationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageOrganizationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageOrganizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
