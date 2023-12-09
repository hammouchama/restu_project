import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UserAuthService } from '../services/user-auth.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private userAuthService: UserAuthService,
    private router: Router,
  ) { }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {

    if (this.userAuthService.getToken() !== null) {
      const role = route.data['role']

      if (role) {
        if (role == this.userAuthService.getRole()) {
          return true;
        } else {
          this.router.navigate(['/forbidden']);
          return false;
        }
      }

    }
    this.router.navigate(['/login'])
    return false;
  }
}
