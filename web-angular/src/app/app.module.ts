import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ArticlePostComponent} from "./articles/article-post/article-post.component";
import {ArticleCardComponent} from "./articles/article-card/article-card.component";
import {ArticleListComponent} from "./articles/article-list/article-list.component";
import {ArticleService} from "./articles/article.service";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {LikeService} from "./articles/like.service";
import {ArticleDeleteComponent} from "./articles/article-delete/article-delete.component";
import {NotificationCardComponent} from "./notifications/notification-card/notification-card.component";
import {NotificationListComponent} from "./notifications/notification-list/notification-list.component";
import {NotificationService} from "./notifications/notification.service";

@NgModule({
  declarations: [
    AppComponent,
    ArticlePostComponent,
    ArticleCardComponent,
    ArticleListComponent,
    ArticleDeleteComponent,
    NotificationCardComponent,
    NotificationListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [ArticleService, LikeService, NotificationService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
