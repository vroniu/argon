import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { AuthService } from './../../auth/services/auth.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'arg-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent implements OnInit {
  @Input() displayMenu = true;
  @Input() displayUserInfo = true;
  @Output() menuButtonPressed = new EventEmitter<any>();
  user: User | null;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    if (this.displayUserInfo) {
      this.user = this.authService.getCurrentUser();
    }
  }

  onMenuButtonClick() {
    this.menuButtonPressed.emit();
  }

  onLogOutButtonClick() {
    this.authService.logOut();
    this.router.navigate(['/']);
  }
}
