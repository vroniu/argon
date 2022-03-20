import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorktimeArchiveComponent } from './worktime-archive.component';

describe('WorktimeArchiveComponent', () => {
  let component: WorktimeArchiveComponent;
  let fixture: ComponentFixture<WorktimeArchiveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorktimeArchiveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorktimeArchiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
