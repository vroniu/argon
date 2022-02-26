import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizationDialogComponent } from './organization-dialog.component';

describe('OrganizationDialogComponent', () => {
  let component: OrganizationDialogComponent;
  let fixture: ComponentFixture<OrganizationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrganizationDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrganizationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
