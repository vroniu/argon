import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthGuard } from './auth.guard';
import { User } from 'src/app/models/user.model';
import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './../services/auth.service';

describe('AuthGuard', () => {
  const router = {
    navigate: jasmine.createSpy('navigate')
  };
  let authService: AuthService;
  let authGuard: AuthGuard;
  let mockSnapshot:any = jasmine.createSpyObj<RouterStateSnapshot>("RouterStateSnapshot", ['toString']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthGuard,
        { provide: Router, useValue: router }
      ]
    }).compileComponents();
    authService = TestBed.inject(AuthService);
    authGuard = TestBed.inject(AuthGuard);
  });

  it('should be able to hit route when user is logged in', () => {
    const testUserData: User = {
      id: 1,
      username: 'testuser',
      email: 'mail@mail.com',
      employee: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe'
      }
    };
    spyOn(authService, 'getCurrentUser').and.returnValue(testUserData);
    expect(authGuard.canActivate(new ActivatedRouteSnapshot(), mockSnapshot)).toBe(true);
  });

  it('not be able to hit route when user is not logged in', () => {
    spyOn(authService, 'getCurrentUser').and.returnValue(null);
    expect(authGuard.canActivate(new ActivatedRouteSnapshot(), mockSnapshot)).toBe(false);
  });
});
