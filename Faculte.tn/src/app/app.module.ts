import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule  } from '@angular/forms';
import { JwtModule } from '@auth0/angular-jwt'; // Import JwtModule
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { IonicModule } from '@ionic/angular';
import { MatDialogModule } from '@angular/material/dialog';
import { AppComponent } from './app.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { HttpClientModule,HttpClient } from '@angular/common/http';
import { SignInComponent } from './sign-in/sign-in.component';
import { TimelineComponent } from './timeline/timeline.component';
import { RegisterComponent } from './register/register.component';
import { StudentloginformComponent } from './studentloginform/studentloginform.component';
import { TeacherloginformComponent } from './teacherloginform/teacherloginform.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { TeacherTimelineComponent } from './teacher-timeline/teacher-timeline.component';
import { ProfilepageComponent } from './profilepage/profilepage.component';
import { PostCommentsComponent } from './post-comments/post-comments.component';
import { ShareModalComponent } from './share-modal/share-modal.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AdminComponent } from './admin/admin.component';
import { AdminmajorComponent } from './adminmajor/adminmajor.component';
import { AdminteacherComponent } from './adminteacher/adminteacher.component';
import { AdmincourseComponent } from './admincourse/admincourse.component';
import { CourseDetailsPopupComponent } from './course-details-popup/course-details-popup.component';
import { ChatDialogComponent } from './chat-dialog/chat-dialog.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { LanguageSwitcherComponent } from './language-switcher/language-switcher.component';
import { AdminreportsComponent } from './adminreports/adminreports.component';
import { NotificationComponent } from './notification/notification.component';
import { ChatListComponent } from './chat-list/chat-list.component';
import { ToastrModule } from 'ngx-toastr';
import { SearchResultsComponent } from './search-results/search-results.component';
import { CourseProposalFormComponent } from './course-proposal-form/course-proposal-form.component'; // Import ToastrModule
import { PdfViewerModule } from 'ng2-pdf-viewer';



export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    SignInComponent,
    TimelineComponent,
    RegisterComponent,
    StudentloginformComponent,
    TeacherloginformComponent,
    ForgotPasswordComponent,
    TeacherTimelineComponent,
    ProfilepageComponent,
    PostCommentsComponent,
    ShareModalComponent,
    AdminComponent,
    AdminmajorComponent,
    AdminteacherComponent,
    AdmincourseComponent,
    CourseDetailsPopupComponent,
    ChatDialogComponent,
    LanguageSwitcherComponent,
    AdminreportsComponent,
    NotificationComponent,
    ChatListComponent,
    SearchResultsComponent,
    CourseProposalFormComponent,
  ],
  imports: [
    BrowserModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule,
    MatDialogModule,
    PdfViewerModule,
    IonicModule.forRoot(),
    AppRoutingModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    JwtModule.forRoot({ // Configure JwtModule
      config: {
        tokenGetter: () => localStorage.getItem('accessToken'),
        allowedDomains: ['example.com'], // Replace with your domain
        disallowedRoutes: ['http://example.com/api/auth/logout'] // Replace with your logout endpoint
      }
    }),
    NgbCarouselModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

