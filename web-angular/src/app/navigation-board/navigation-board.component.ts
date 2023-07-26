import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'navigation-board',
  templateUrl: './navigation-board.component.html',
  styleUrls: ['./navigation-board.component.scss']
})
export class NavigationBoardComponent implements OnInit {

  ngOnInit(): void {
  }

  navigateToArticles(): void {
    const articles = document.querySelector('articles')
    const notifications = document.querySelector('notifications')
    // @ts-ignore
    articles.style.display = 'flex';
    // @ts-ignore
    notifications.style.display = 'none';
  }

  navigateToNotifications(): void {
    const articles = document.querySelector('articles')
    const notifications = document.querySelector('notifications')
    // @ts-ignore
    articles.style.display = 'none';
    // @ts-ignore
    notifications.style.display = 'flex';
  }

}
