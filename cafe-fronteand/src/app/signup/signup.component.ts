import { NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserService } from '../services/user.service';
import { UserAuthService } from '../services/user-auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  hide: any;
  email: any;
  errorMessa: string = ""
  error: boolean = false
  constructor(private userServce: UserService,
    private router: Router) {

  }

  singup(loginForm: NgForm) {
    this.userServce.signup(loginForm.value).subscribe(
      (response: any) => {

        if (response.message) {
          this.router.navigate(["/login"])
        }
      },
      (error) => {
        this.errorMessa = "Some thing is wrong !"
        this.error = true;

      }
    )
  }

}
