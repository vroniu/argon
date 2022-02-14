import { AppRoutingModule } from './../../app-routing.module';
import { MatIconModule } from '@angular/material/icon';
import { AuthModule } from './../../auth/auth.module';
import { TopBarComponent } from './top-bar.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [TopBarComponent],
  imports: [ CommonModule, AuthModule, MatIconModule, MatButtonModule, AppRoutingModule ],
  exports: [ TopBarComponent ],
  providers: [],
})
export class TopBarModule {}
