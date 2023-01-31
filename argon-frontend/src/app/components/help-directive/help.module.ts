import { MatRippleModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { HelpComponent } from './help.component';
import { MatButtonModule } from '@angular/material/button';
import { HelpDirective } from './help.directive';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [ HelpDirective, HelpComponent ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatRippleModule
  ],
  exports: [ HelpDirective ],
  providers: [],
})
export class HelpModule {}
