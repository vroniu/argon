import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmitWorktimeComponent } from './submit-worktime.component';

describe('SubmitWorkdayComponent', () => {
  let component: SubmitWorktimeComponent;
  let fixture: ComponentFixture<SubmitWorktimeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubmitWorktimeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmitWorktimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
