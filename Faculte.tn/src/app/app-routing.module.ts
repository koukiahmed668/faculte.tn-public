import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignInComponent } from './sign-in/sign-in.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { RegisterComponent } from './register/register.component';
import { StudentloginformComponent } from './studentloginform/studentloginform.component';
import { TeacherloginformComponent } from './teacherloginform/teacherloginform.component';
import { TimelineComponent } from './timeline/timeline.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { TeacherTimelineComponent } from './teacher-timeline/teacher-timeline.component';
import { ProfilepageComponent } from './profilepage/profilepage.component';
import { AdminComponent } from './admin/admin.component';
import { AdminmajorComponent } from './adminmajor/adminmajor.component';
import { AdminteacherComponent } from './adminteacher/adminteacher.component';
import { AdmincourseComponent } from './admincourse/admincourse.component';
import { ChatDialogComponent } from './chat-dialog/chat-dialog.component';
import { AdminreportsComponent } from './adminreports/adminreports.component';
import { NotificationComponent } from './notification/notification.component';
import { SearchResultsComponent } from './search-results/search-results.component'; // Import ToastrModule
import { AuthGuardService } from './Services/auth-guard.service';
import { AdminAuthGuardService } from './Services/admin-auth-guard.service';
import { TeacherAuthGuardService } from './Services/teacher-auth-guard.service';





const routes: Routes = [
  { path: '', redirectTo: '/landing-page', pathMatch: 'full' },
  { path: 'landing-page', component: LandingPageComponent },
  { path: 'sign-in', component: SignInComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'studentloginform', component: StudentloginformComponent },
  { path: 'teacherloginform', component: TeacherloginformComponent },
  { path: 'timeline', component: TimelineComponent,canActivate:[AuthGuardService] },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'profile/:userId', component: ProfilepageComponent,canActivate:[AuthGuardService] },
  { path: 'teachertimeline', component: TeacherTimelineComponent,canActivate:[TeacherAuthGuardService] },
  { path: 'admin', component: AdminComponent,canActivate:[AdminAuthGuardService] },
  { path: 'adminmajorsubject', component: AdminmajorComponent,canActivate:[AdminAuthGuardService] },
  { path: 'adminteacher', component: AdminteacherComponent,canActivate:[AdminAuthGuardService] },
  { path: 'admincourse', component: AdmincourseComponent,canActivate:[AdminAuthGuardService] },
  { path: 'adminreport', component: AdminreportsComponent,canActivate:[AdminAuthGuardService] },
  { path: 'chatpopup/:friendId', component: ChatDialogComponent,canActivate:[AuthGuardService] },
  { path: 'notif', component: NotificationComponent,canActivate:[AuthGuardService] },
  { path: 'search-results', component: SearchResultsComponent,canActivate:[AuthGuardService] },

];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
