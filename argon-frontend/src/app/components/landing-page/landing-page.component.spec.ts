import { Component, Input } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandingPageComponent } from './landing-page.component';

@Component({ selector: 'arg-top-bar', template: '' })
class TopBarStubComponent {
  @Input() displayMenu = true;
  @Input() displayUserInfo = true;
}

describe('LandingPageComponent', () => {
  let component: LandingPageComponent;
  let fixture: ComponentFixture<LandingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        LandingPageComponent,
        TopBarStubComponent
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
