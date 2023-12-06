import { NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserService } from '../services/user.service';
import { UserAuthService } from '../services/user-auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  hide: any;
  email: any;
  constructor(private userServce: UserService,
    private userAthiService: UserAuthService,
    private router: Router) {

  }
  ngOnInit(): void {

  }
  login(loginForm: NgForm) {
    console.log(loginForm.value)
    this.userServce.login(loginForm.value).subscribe(
      (response: any) => {
        this.userAthiService.setRole(response.role)
        this.userAthiService.setToken(response.token)
        this.router.navigate(["/home"])
      },
      (error) => { console.log(error) }
    )
  }
}
