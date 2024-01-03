import {Component, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {AuthorizationService} from './auth/service/authorization.service';
import {DataStorageService} from './shared/data-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'web-angular';
  currentLanguage = 'en';

  constructor(
    private translate: TranslateService,
    private authorizationService: AuthorizationService,
    private dataStorageService: DataStorageService
  ) {
  }

  ngOnInit(): void {
    this.translate.use(this.currentLanguage);
    this.setupInteractionsListener();
  }

  changeLanguage(): void {
    this.currentLanguage = this.currentLanguage === 'en' ? 'pl' : 'en';
    this.translate.use(this.currentLanguage);
  }

  private setupInteractionsListener() {
    document.addEventListener('mousemove', () => {
      if (this.authorizationService.isSessionExpired()) {
        if (this.authorizationService.isRefreshTokenExpired()) {
          this.authorizationService.logout();
        } else {
          this.dataStorageService.refreshToken();
        }
      }
    });
  }
}
