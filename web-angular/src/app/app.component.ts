import {Component} from '@angular/core';
import {AppService} from "./app.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [AppService]
})
export class AppComponent {
  title = 'web-angular';

  constructor(private appService: AppService) {
  }

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

  updateArticleList(event: Event): void {
    const inputText = (event.target as HTMLInputElement).value;
    this.appService.updateArticleList(inputText);
  }
}
