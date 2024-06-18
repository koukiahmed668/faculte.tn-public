// language-switcher.component.ts
import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthenticationService } from '../Services/authentication.service';

@Component({
  selector: 'app-language-switcher',
  templateUrl: './language-switcher.component.html',
  styleUrls: ['./language-switcher.component.css']
})
export class LanguageSwitcherComponent {
  supportedLanguages = [
    { code: 'en', name: 'English' },
    { code: 'fr', name: 'French' },
    // Add more languages as needed
  ];

  showPopup: boolean = false; // Define the showPopup property

  togglePopup() {
    this.showPopup = !this.showPopup;
  }
  
  constructor(private translate: TranslateService, private authService: AuthenticationService) {}

  changeLanguage(languageCode: string) {
    this.translate.use(languageCode);
    localStorage.setItem('preferredLanguage', languageCode);
  }

  logout(): void {
    this.authService.logout();
  }
  
}
