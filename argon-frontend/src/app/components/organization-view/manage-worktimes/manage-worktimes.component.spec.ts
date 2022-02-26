import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageWorktimesComponent } from './manage-worktimes.component';

describe('ManageWorktimesComponent', () => {
  let component: ManageWorktimesComponent;
  let fixture: ComponentFixture<ManageWorktimesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageWorktimesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageWorktimesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
