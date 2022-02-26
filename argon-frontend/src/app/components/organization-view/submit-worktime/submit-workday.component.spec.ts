import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmitWorktimeComponent } from './submit-worktime.component';

describe('SubmitWorktimeComponent', () => {
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

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
