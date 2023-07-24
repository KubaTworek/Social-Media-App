import { Component } from '@angular/core';
import {ArticleRequest} from "./articles/dto/article-request.type";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'web-angular';

  navigateToArticles(): void {
    const articlePost = document.querySelector('article-post')
    const articles = document.querySelector('article-list')
    const notifications = document.querySelector('notification-list')
    // @ts-ignore
    articlePost.style.display = 'flex';
    // @ts-ignore
    articles.style.display = 'flex';
    // @ts-ignore
    notifications.style.display = 'none';
  }

  navigateToNotifications(): void {
    const articlePost = document.querySelector('article-post')
    const articles = document.querySelector('article-list')
    const notifications = document.querySelector('notification-list')
    // @ts-ignore
    articlePost.style.display = 'none';
    // @ts-ignore
    articles.style.display = 'none';
    // @ts-ignore
    notifications.style.display = 'flex';
  }
}
