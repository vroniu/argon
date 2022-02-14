import { AuthModule } from './../../auth/auth.module';
import { MainPageComponent } from './main-page.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TopBarModule } from '../top-bar/top-bar.module';

@NgModule({
  declarations: [ MainPageComponent ],
  imports: [ CommonModule, TopBarModule, AuthModule ],
  exports: [],
  providers: [],
})
export class MainPageModule {}
