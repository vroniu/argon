import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorktimeFormComponent } from './worktime-form.component';

describe('WorktimeFormComponent', () => {
  let component: WorktimeFormComponent;
  let fixture: ComponentFixture<WorktimeFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorktimeFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorktimeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
