import { Component, Input } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainPageComponent } from './main-page.component';

@Component({ selector: 'arg-top-bar', template: '' })
class TopBarStubComponent {
  @Input() displayMenu = true;
  @Input() displayUserInfo = true;
}

describe('MainPageComponent', () => {
  let component: MainPageComponent;
  let fixture: ComponentFixture<MainPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        MainPageComponent,
        TopBarStubComponent
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MainPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
