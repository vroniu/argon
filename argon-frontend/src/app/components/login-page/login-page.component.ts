import { ToastService } from './../../services/toast.service';
import { RegistrationRequest } from './../../auth/models/register.model';
import { AuthService } from './../../auth/services/auth.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { LoginRequest } from '../../auth/models/login.model';
import { Employee } from 'src/app/models/employee.model';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'arg-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  @ViewChild('loginForm') loginForm: FormGroup;
  @ViewChild('registerForm') registerForm: FormGroup;
  hideLoginPassword = true;
  hideRegisterPassword = true;
  selectedTab: number;

  loginRequest: LoginRequest;
  registerRequest: RegistrationRequest;
  passwordConfirm: string;

  constructor(private authService: AuthService, private toastService: ToastService, private router: Router) { }

  ngOnInit(): void {
    this.loginRequest = new LoginRequest();
    this.registerRequest = new RegistrationRequest();
    this.registerRequest.employee = new Employee();
  }

  onLoginSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginRequest).subscribe(
        () => {
          this.authService.currentUser.subscribe(() => {
            this.router.navigate(['/main/organizations']);
          });
        },
        (error) => {
          if (error.status === 400) {
            this.toastService.addErrorToast(error.error.description);
          }
        }
      );
    }
  }

  onRegisterSubmit() {
    if (this.registerForm.valid && this.validEmail() && this.validPasswordStrenght() && this.validPasswordsMatch()) {
      this.authService.register(this.registerRequest).subscribe(
        () => {
          this.toastService.addSuccessToast('User created succesfully! You can now log in.');
          this.registerForm.reset();
          this.selectedTab = 0;
        },
        (error) => {
          if (error.error && error.status === 400) {
            this.toastService.addErrorToast(error.error.description);
          }
        }
      );
    }
  }

  validEmail() {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(this.registerRequest.email);
  }

  validPasswordStrenght() {
    const re = /^(?=.*\d)[0-9a-zA-Z!@#&()-_{}:;',?/*~$^+=<>]{8,}$/;
    return re.test(this.registerRequest.password);
  }

  validPasswordsMatch() {
    return this.registerRequest.password.localeCompare(this.passwordConfirm) === 0;
  }

}
