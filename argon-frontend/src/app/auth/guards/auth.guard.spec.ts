import { RouterTestingModule } from '@angular/router/testing';
import { MockLocationStrategy } from '@angular/common/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthGuard } from './auth.guard';
import { User } from 'src/app/models/user.model';
import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './../services/auth.service';
import { routes } from 'src/app/app-routing.module';
import {Location, LocationStrategy} from '@angular/common';

describe('AuthGuard', () => {
  let router: Router;
  let location: Location;
  let authService: AuthService;
  let authGuard: AuthGuard;
  let mockSnapshot:any = jasmine.createSpyObj<RouterStateSnapshot>("RouterStateSnapshot", ['toString']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes(routes)
      ],
      providers: [
        AuthGuard,
        { provide: LocationStrategy, useClass: MockLocationStrategy },
      ]
    }).compileComponents();
    authService = TestBed.inject(AuthService);
    authGuard = TestBed.inject(AuthGuard);
    location = TestBed.inject(Location);
    router = TestBed.inject(Router);
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

  it('should not be able to hit route when user is not logged in', () => {
    spyOn(authService, 'getCurrentUser').and.returnValue(null);
    spyOn(router, 'navigate');
    expect(authGuard.canActivate(new ActivatedRouteSnapshot(), mockSnapshot)).toBe(false);
    expect(router.navigate).toHaveBeenCalled();
  });
});
