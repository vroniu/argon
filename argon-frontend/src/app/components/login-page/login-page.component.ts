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
        (response) => {
          if (response) {
            this.router.navigate(['/main']);
          } else {
            this.toastService.addErrorToast('Something went wrong.');
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
          if (error.error && error.error.status) {
            this.toastService.addErrorToast(error.error.description);
          } else {
            this.toastService.addErrorToast('Something went wrong');
          }
        }
      );
    }
  }

  validEmail() {
    const re = /\S+@\S+\.\S+/;
    return re.test(this.registerRequest.email);
  }

  validPasswordStrenght() {
    return this.registerRequest.password.match(/^(?=.*\d)[0-9a-zA-Z!@#&()-_{}:;',?/*~$^+=<>]{8,}$/);
  }

  validPasswordsMatch() {
    return this.registerRequest.password.localeCompare(this.passwordConfirm) === 0;
  }

}
