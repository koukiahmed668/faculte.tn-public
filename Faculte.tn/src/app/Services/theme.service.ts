import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  isDarkMode: boolean = false;
  constructor() { }
  toggleDarkMode(): void {
    this.isDarkMode = !this.isDarkMode;
    this.updateTheme(this.isDarkMode);
  }

  private updateTheme(isDarkMode: boolean): void {
    const theme = isDarkMode ? 'dark-mode' : '';
    document.documentElement.className = theme;
  }
}
