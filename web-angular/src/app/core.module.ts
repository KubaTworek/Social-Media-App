import {ArticleService} from "./articles/service/article.service";
import {NotificationService} from "./notifications/service/notification.service";
import {AuthorizationService} from "./auth/service/authorization.service";
import {NgModule} from "@angular/core";
import {SearchService} from "./search-board/service/search.service";
import {AuthorsService} from "./authors/service/authors.service";

@NgModule({
  providers: [
    ArticleService,
    NotificationService,
    AuthorsService,
    AuthorizationService,
    SearchService
  ]
})
export class CoreModule {
}
