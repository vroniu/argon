import { AppModule } from './../../app.module';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AuthModule } from 'src/app/auth/auth.module';

import { TopBarComponent } from './top-bar.component';

describe('TopBarComponent', () => {
  let component: TopBarComponent;
  let fixture: ComponentFixture<TopBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopBarComponent ],
      imports: [ AppModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopBarComponent);
    component = fixture.componentInstance;
  });

  it('should create when displayMenu and displayUserInfo is false', () => {
    component.displayMenu = false;
    component.displayUserInfo = false;
    fixture.detectChanges();
    component.ngOnInit();
    expect(component).toBeTruthy();
  });
});
