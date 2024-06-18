import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './user.service';
import { JwtHelperService } from '@auth0/angular-jwt'; // Import JwtHelperService
import { environment } from '../../environments/environment';
const apiUrl = environment.apiUrl;

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private router: Router, private userService: UserService, private jwtHelper: JwtHelperService) { }

  initializeApp(): void {
    const token = this.userService.getToken();
    if (!token) {
      // Token does not exist, redirect to landing page or login
      this.router.navigateByUrl('/landing-page');
    } else {
      // Token exists, user is authenticated
      const role = this.getRoleFromToken(token);
      this.userService.redirectBasedOnRole(role);
    }
  }

  private getRoleFromToken(token: string): string {
    const decodedToken = this.jwtHelper.decodeToken(token);
    return decodedToken.roles;
  }


  logout(): void {
    localStorage.removeItem('accessToken');
    this.router.navigateByUrl('/sign-in');
  }


}
