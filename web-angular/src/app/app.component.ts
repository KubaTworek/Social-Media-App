import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {AuthorizationService} from "./auth/service/authorization.service";
import {DataStorageService} from "./shared/data-storage.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'web-angular';
  currentLanguage: string = 'en';

  constructor(
    private translate: TranslateService,
    private authorizationService: AuthorizationService,
    private dataStorageService: DataStorageService,
    ) {
    this.translate.use(this.currentLanguage);
    this.setupInteractionsListener();
  }

  changeLanguage(): void {
    if (this.currentLanguage == 'en') {
      this.currentLanguage = 'pl';
    } else {
      this.currentLanguage = 'en';
    }
    this.translate.use(this.currentLanguage);
  }

  private setupInteractionsListener() {
    document.addEventListener("mousemove", () => {
      if (this.authorizationService.isSessionExpired()) {
        if(this.authorizationService.isRefreshTokenExpired()) {
          this.authorizationService.logout();
        } else {
          this.dataStorageService.refreshToken();
        }
      }
    });
  }
}
