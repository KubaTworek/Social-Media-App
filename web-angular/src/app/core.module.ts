import {ArticleService} from "./articles/service/article.service";
import {NotificationService} from "./notifications/service/notification.service";
import {AuthorizationService} from "./auth/service/authorization.service";
import {NgModule} from "@angular/core";
import {SearchService} from "./search-board/service/search.service";

@NgModule({
  providers: [
    ArticleService,
    NotificationService,
    AuthorizationService,
    SearchService
  ]
})
export class CoreModule {
}
