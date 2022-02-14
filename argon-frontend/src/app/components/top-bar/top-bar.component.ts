import { User } from 'src/app/models/user.model';
import { AuthService } from './../../auth/services/auth.service';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'arg-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent implements OnInit {
  @Input() displayMenu = true;
  @Input() displayUserInfo = true;
  user: User | null;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    if (this.displayUserInfo) {
      this.user = this.authService.getCurrentUser();
    }
  }

}
