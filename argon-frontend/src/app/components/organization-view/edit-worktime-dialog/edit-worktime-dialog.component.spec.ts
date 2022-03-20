import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditWorktimeDialogComponent } from './edit-worktime-dialog.component';

describe('EditWorktimeDialogComponent', () => {
  let component: EditWorktimeDialogComponent;
  let fixture: ComponentFixture<EditWorktimeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditWorktimeDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditWorktimeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
