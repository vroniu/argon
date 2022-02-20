import { ToastService } from './../../services/toast.service';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { AppModule } from './../../app.module';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { LoginPageComponent } from './login-page.component';
import { Component, Input, DebugElement } from '@angular/core';
import { AuthService } from 'src/app/auth/services/auth.service';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { routes } from 'src/app/app-routing.module';
import { TestUtils } from 'src/tests/test-utils';

@Component({ selector: 'arg-top-bar', template: '' })
class TopBarStubComponent {
  @Input() displayMenu = true;
  @Input() displayUserInfo = true;
}

describe('LoginPageComponent', () => {
  let component: LoginPageComponent;
  let fixture: ComponentFixture<LoginPageComponent>;
  let authService: AuthService;
  let router: Router;
  let toastService: ToastService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        LoginPageComponent,
        TopBarStubComponent
      ],
      imports: [
        AppModule,
        RouterTestingModule.withRoutes(routes)
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(LoginPageComponent);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    toastService = TestBed.inject(ToastService);
    fixture.detectChanges();
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('validPasswordStrenght should return true for all valid passwords', () => {
    const validPasswords = [
      'password1',
      'ComPlIcAtEdPassWord1',
      'simple_password23',
      '123password',
      'd5t6753785byu35byu',
      'AAAA111AAAA'
    ];
    validPasswords.forEach(password => {
      component.registerRequest.password = password;
      expect(component.validPasswordStrenght()).toBeTruthy();
    });
  });

  it('validPasswordStrenght should return false for all invalid passwords', () => {
    const invalidPasswords = [
      'short',
      'ComPlIcAtEdPassWord',
      '__________',
      '',
      'nodigitman'
    ];
    invalidPasswords.forEach(password => {
      component.registerRequest.password = password;
      expect(component.validPasswordStrenght()).toBeFalsy();
    });
  });

  it('validEmail should return true for all valid emails', () => {
    const validEmails = [
      'email@example.com',
      'firstname.lastname@example.com',
      'email@subdomain.example.com',
      '1234567890@example.com',
      'email@example-one.com',
      '_______@example.com',
      'email@example.name',
      'email@example.museum',
      'email@example.co.jp',
      'firstname-lastname@example.com'
    ];
    validEmails.forEach(email => {
      component.registerRequest.email = email;
      expect(component.validEmail()).toBeTruthy();
    });
  });

  it('validEmail should return false for all invalid emails', () => {
    const invalidEmails = [
      'plainaddress',
      '#@%^%#$@#$@#.com',
      '@example.com',
      'Joe Smith <email@example.com>',
      'email.example.com',
      'email@example@example.com',
      '.email@example.com',
      'email.@example.com',
      'email..email@example.com'
    ];
    invalidEmails.forEach(email => {
      component.registerRequest.email = email;
      expect(component.validEmail()).toBeFalsy();
    });
  });

  it('validPasswordsMatch should return true if passwords are matching', () => {
    component.registerRequest.password = 'password';
    component.passwordConfirm = 'password';
    expect(component.validPasswordsMatch()).toBeTrue();
  });

  it('validPasswordsMatch should return false if passwords are not matching', () => {
    component.registerRequest.password = 'password';
    component.passwordConfirm = 'notpassword';
    expect(component.validPasswordsMatch()).toBeFalse();
  });

  it('LoginForm should display info when form is invalid', fakeAsync(() => {
    let loginSpy = spyOn(authService, 'login');

    TestUtils.inputValuesToFields(fixture, [
      { id: 'username', value: '' },
      { id: 'password', value: '' },
    ]);
    TestUtils.clickButton(fixture, 'login-submit');
    fixture.detectChanges();
    tick();

    let invalidInputHints: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-hint'));
    expect(component.loginForm.valid).toBeFalse();
    expect(component.loginForm.touched).toBeTrue();
    expect(invalidInputHints.length).toEqual(2);
    expect(invalidInputHints[0].nativeElement.textContent.trim()).toEqual('Username is required.');
    expect(invalidInputHints[1].nativeElement.textContent.trim()).toEqual('Password is required.');
    expect(loginSpy).not.toHaveBeenCalled();
  }));

  it('LoginForm should display toast when credentials are invalid', fakeAsync(() => {
    let loginSpy = spyOn(authService, 'login').and.returnValue(throwError({
      status: 400,
      statusText: 'Bad credentials',
      error: {
        status: 'BAD_CREDENTIALS',
        description: 'Incorrect username or password'
      }
    }));
    let toastSpy = spyOn(toastService, 'addErrorToast').and.callFake(() => { });

    TestUtils.inputValuesToFields(fixture, [
      { id: 'username', value: 'username' },
      { id: 'password', value: 'password' },
    ]);
    TestUtils.clickButton(fixture, 'login-submit');
    fixture.detectChanges();
    tick();

    expect(loginSpy).toHaveBeenCalled();
    expect(toastSpy).toHaveBeenCalledWith('Incorrect username or password');
  }));

  it('LoginForm should log in the user when the form is valid', fakeAsync(() => {
    let loginSpy = spyOn(authService, 'login').and.returnValue(of(true));
    let routerSpy = spyOn(router, 'navigate');

    TestUtils.inputValuesToFields(fixture, [
      { id: 'username', value: 'username' },
      { id: 'password', value: 'password' },
    ]);
    TestUtils.clickButton(fixture, 'login-submit');
    fixture.detectChanges();
    tick();

    let invalidInputHints: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-hint'));
    expect(component.loginForm.valid).toBeTrue();
    expect(component.loginForm.touched).toBeTrue();
    expect(invalidInputHints.length).toEqual(0);
    expect(loginSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/main']);
  }));

  it('LoginForm should be able to hide and show the password', fakeAsync(() => {
    let passwordInputEl: DebugElement = fixture.debugElement.query(By.css('#password'));
    let visibilityToggleEl: DebugElement = fixture.debugElement.query(By.css('#password-toggle'));
    expect(passwordInputEl.attributes.type).toEqual('password');

    visibilityToggleEl.nativeElement.click();
    fixture.detectChanges();
    tick();
    expect(passwordInputEl.attributes.type).toEqual('text');

    visibilityToggleEl.nativeElement.click();
    fixture.detectChanges();
    tick();
    expect(passwordInputEl.attributes.type).toEqual('password');
  }));

  it('RegisterForm should display info when form is invalid', fakeAsync(() => {
    let registerSpy = spyOn(authService, 'register');
    component.selectedTab = 1;
    fixture.detectChanges();
    tick();

    TestUtils.inputValuesToFields(fixture, [
      { id: 'username-register', value: '' },
      { id: 'password-register', value: '' },
      { id: 'password-confirm', value: '' },
      { id: 'email', value: '' },
      { id: 'first-name', value: '' },
      { id: 'last-name', value: '' },
    ]);
    TestUtils.clickButton(fixture, 'register-submit');
    fixture.detectChanges();
    tick();

    let invalidInputHints: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-hint'));
    expect(component.registerForm.valid).toBeFalse();
    expect(component.registerForm.touched).toBeTrue();
    expect(invalidInputHints.length).toEqual(5);
    expect(invalidInputHints[0].nativeElement.textContent.trim()).toEqual('Username is required.');
    expect(invalidInputHints[1].nativeElement.textContent.trim()).toEqual('Email is required.');
    expect(invalidInputHints[2].nativeElement.textContent.trim()).toEqual('First name is required.');
    expect(invalidInputHints[3].nativeElement.textContent.trim()).toEqual('Last name is required.');
    expect(invalidInputHints[4].nativeElement.textContent.trim()).toEqual('Password is required.');
    expect(registerSpy).not.toHaveBeenCalled();
  }));

  it('RegisterForm should display info when passwords dont match', fakeAsync(() => {
    component.selectedTab = 1;
    fixture.detectChanges();
    tick();

    TestUtils.inputValuesToFields(fixture, [
      { id: 'password-register', value: 'password1' },
      { id: 'password-confirm', value: 'password111' },
    ]);
    TestUtils.clickButton(fixture, 'register-submit');
    fixture.detectChanges();
    tick();

    let invalidInputHints: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-hint'));
    expect(component.registerForm.valid).toBeFalse();
    expect(invalidInputHints.length).toEqual(3);
    expect(invalidInputHints[2].nativeElement.textContent.trim()).toEqual('Passwords do not match.');
  }));

  it('RegisterForm should display info when password is not strong enough', fakeAsync(() => {
    component.selectedTab = 1;
    fixture.detectChanges();
    tick();

    TestUtils.inputValuesToFields(fixture, [
      { id: 'password-register', value: 'pwd' },
      { id: 'password-confirm', value: 'pwd' },
    ]);
    TestUtils.clickButton(fixture, 'register-submit');
    fixture.detectChanges();
    tick();

    let invalidInputHints: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-hint'));
    expect(component.registerForm.valid).toBeFalse();
    expect(invalidInputHints.length).toEqual(2);
    expect(invalidInputHints[1].nativeElement.textContent.trim()).toEqual('Password should be at least 8 characters long and contain at least one digit.');
  }));

  it('RegisterForm should display info when email is not valid', fakeAsync(() => {
    component.selectedTab = 1;
    fixture.detectChanges();
    tick();

    TestUtils.inputValueToField(fixture, 'email', 'notavalidemail');
    fixture.detectChanges();
    tick();

    let invalidInputHints: DebugElement[] = fixture.debugElement.queryAll(By.css('mat-hint'));
    expect(component.registerForm.valid).toBeFalse();
    expect(invalidInputHints.length).toEqual(2);
    expect(invalidInputHints[0].nativeElement.textContent.trim()).toEqual('Invalid email format.');
  }));

  it('RegisterForm should display error toast when authService throws an error', fakeAsync(() => {
    let registerSpy = spyOn(authService, 'register').and.returnValue(throwError({
      status: 400,
      statusText: 'Bad credentials',
      error: {
        status: 'BAD_CREDENTIALS',
        description: 'Incorrect username or password'
      }
    }));
    let toastSpy = spyOn(toastService, 'addErrorToast').and.callFake(() => { });
    component.selectedTab = 1;
    fixture.detectChanges();
    tick();

    TestUtils.inputValuesToFields(fixture, [
      { id: 'username-register', value: 'username' },
      { id: 'password-register', value: 'password1' },
      { id: 'password-confirm', value: 'password1' },
      { id: 'email', value: 'mail@mail.com' },
      { id: 'first-name', value: 'John' },
      { id: 'last-name', value: 'Doe' },
    ]);
    TestUtils.clickButton(fixture, 'register-submit');
    fixture.detectChanges();
    tick();

    expect(component.registerForm.valid).toBeTrue();
    expect(registerSpy).toHaveBeenCalled();
    expect(toastSpy).toHaveBeenCalledWith('Incorrect username or password');
  }));

  it('RegisterForm should show toast message, reset RegisterForm and open LoginForm when user registers correctly', fakeAsync(() => {
    let registerSpy = spyOn(authService, 'register').and.returnValue(of({
      status: 'SUCCESS',
      descrription: 'User created successfully'
    }));
    let toastSpy = spyOn(toastService, 'addSuccessToast').and.callFake(() => { });
    component.selectedTab = 1;
    fixture.detectChanges();
    tick();

    TestUtils.inputValuesToFields(fixture, [
      { id: 'username-register', value: 'username' },
      { id: 'password-register', value: 'password1' },
      { id: 'password-confirm', value: 'password1' },
      { id: 'email', value: 'mail@mail.com' },
      { id: 'first-name', value: 'John' },
      { id: 'last-name', value: 'Doe' },
    ]);
    TestUtils.clickButton(fixture, 'register-submit');
    fixture.detectChanges();
    tick();

    expect(registerSpy).toHaveBeenCalled();
    expect(toastSpy).toHaveBeenCalledWith('User created succesfully! You can now log in.');
    expect(component.registerForm.pristine).toBeTrue();
    expect(component.selectedTab).toEqual(0);
  }));
});
