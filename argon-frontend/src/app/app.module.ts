import { MainPageModule } from './components/main-page/main-page.module';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatFormFieldModule, MAT_FORM_FIELD_DEFAULT_OPTIONS, MatHint } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input'
import { MatTabsModule } from '@angular/material/tabs';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { FormsModule } from '@angular/forms';
import { AuthModule } from './auth/auth.module';
import { TopBarModule } from './components/top-bar/top-bar.module';
import { SharedModule } from './shared.module';
import { LoginComponent } from './components/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    LandingPageComponent,
    LoginComponent
  ],
  imports: [
    SharedModule,
    MainPageModule,
    TopBarModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
