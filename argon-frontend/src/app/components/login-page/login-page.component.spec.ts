import { AppModule } from './../../app.module';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginPageComponent } from './login-page.component';
import { Component, Input } from '@angular/core';

@Component({ selector: 'arg-top-bar', template: '' })
class TopBarStubComponent {
  @Input() displayMenu = true;
  @Input() displayUserInfo = true;
}

describe('LoginPageComponent', () => {
  let component: LoginPageComponent;
  let fixture: ComponentFixture<LoginPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        LoginPageComponent,
        TopBarStubComponent
      ],
      imports: [AppModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
