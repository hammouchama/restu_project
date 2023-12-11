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
  errorMessa: string = ""
  error: boolean = false
  constructor(private userServce: UserService,
    private userAthiService: UserAuthService,
    private router: Router) {

  }
  ngOnInit(): void {

  }
  login(loginForm: NgForm) {
    this.userServce.login(loginForm.value).subscribe(
      (response: any) => {
        this.userAthiService.setRole(response.role)
        this.userAthiService.setToken(response.token)
        const role = response.role
        console.log(response)
        if (role == "Admin") {
          this.router.navigate(["/admin"])
        } else {
          this.router.navigate(["/home"])
        }

      },
      (error) => {
        this.errorMessa = "Bad Credentials."
        this.error = true;
        console.log(error)

      }
    )
  }
}
