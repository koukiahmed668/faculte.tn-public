import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from './user.service';
@Injectable({
  providedIn: 'root'
})
export class TeacherAuthGuardService {

  constructor(private userService: UserService, private router: Router) {}

  canActivate(): boolean {
    if (this.userService.isAuthenticated() && this.userService.isTeacher()) {
      return true; // Allow access if user is authenticated and has admin role
    } else {
      // Redirect or handle unauthorized access
      this.router.navigateByUrl('/'); // Redirect to home page or any other appropriate action
      return false;
    }
  }}
