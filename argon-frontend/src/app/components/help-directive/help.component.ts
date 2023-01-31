import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { HelpEntry } from './help.directive';
import { MatRipple } from '@angular/material/core';

@Component({
  selector: 'arg-help',
  templateUrl: './help.component.html'
})
export class HelpComponent {
  @Input() help: HelpEntry;
  showHelp: boolean = false;

  toggleHelp() {
    this.showHelp = !this.showHelp;
  }
}
