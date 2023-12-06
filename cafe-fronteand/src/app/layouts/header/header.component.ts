import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserAuthService } from 'src/app/services/user-auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  constructor(
    private useAthuService: UserAuthService,
    private router: Router) { }
  ngOnInit(): void {

  }
  logout() {
    this.useAthuService.clear()
    this.router.navigate(["/home"])
  }
  isLoggedIn() {
    return this.useAthuService.isLoggedIn()
  }

}
