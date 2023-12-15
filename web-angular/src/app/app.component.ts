import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'web-angular';
  currentLanguage: string = 'en';

  constructor(private translate: TranslateService) {
    this.translate.use(this.currentLanguage);
  }

  changeLanguage(): void {
    if (this.currentLanguage == 'en') {
      this.currentLanguage = 'pl';
    } else {
      this.currentLanguage = 'en';
    }
    this.translate.use(this.currentLanguage);
  }
}
