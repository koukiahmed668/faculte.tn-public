import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthenticationService } from './Services/authentication.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Faculte.tn';
  private initialized = false;
  constructor(private translate: TranslateService,private authService: AuthenticationService) {}

  ngOnInit() {
    /*if (!this.initialized) {
      this.authService.initializeApp();
      this.initialized = true;
    }*/   
    const preferredLanguage = localStorage.getItem('preferredLanguage');
    if (preferredLanguage) {
      this.translate.setDefaultLang(preferredLanguage);
      this.translate.use(preferredLanguage);
    } else {
      // Get the browser's language
      const browserLang = this.translate.getBrowserLang();
      // Check if the browser language is available and not undefined
      if (browserLang) {
        this.translate.use(browserLang);
      } else {
        // Fallback to a default language if the browser language is not available
        this.translate.setDefaultLang('en'); // Change 'en' to your default language
        this.translate.use('en');
      }
    }
  }
}
