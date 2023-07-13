import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {ArticlePostComponent} from "./articles/article-post/article-post.component";
import {ArticleCardComponent} from "./articles/article-card/article-card.component";
import {ArticleListComponent} from "./articles/article-list/article-list.component";
import {ArticleService} from "./articles/article.service";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {LikeService} from "./articles/like.service";
import {ArticleDeleteComponent} from "./articles/article-delete/article-delete.component";

@NgModule({
  declarations: [
    AppComponent,
    ArticlePostComponent,
    ArticleCardComponent,
    ArticleListComponent,
    ArticleDeleteComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [ArticleService, LikeService],
  bootstrap: [AppComponent]
})
export class AppModule { }
