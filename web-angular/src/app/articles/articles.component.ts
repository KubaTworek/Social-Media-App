import {Component, OnInit} from '@angular/core';
import {DataStorageService} from "../shared/data-storage.service";
import {ArticleService} from "./service/article.service";
import {Article} from "./dto/article.type";

@Component({
  selector: 'articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {

  isForYouActive = true;
  isFollowingActive = false;

  constructor(
    private dataStorageService: DataStorageService,
    private articlesService: ArticleService
  ) {
  }

  ngOnInit(): void {
  }

  getAllArticles(): void {
    this.articlesService.updateActiveStatus(true, false);
    this.isForYouActive = true;
    this.isFollowingActive = false;

    const articles: Article[] = [];
    this.dataStorageService.fetchArticles(0, 5).subscribe(
      (newArticles: Article[]) => {
        articles.push(...newArticles);
        this.articlesService.setArticlesAndNotify(articles);
      },
      (error) => {
        console.error('Error fetching more articles:', error);
      }
    );
  }

  getFollowedArticles(): void {
    this.articlesService.updateActiveStatus(false, true);
    this.isForYouActive = false;
    this.isFollowingActive = true;

    const articles: Article[] = [];
    this.dataStorageService.fetchFollowingArticles(0, 5).subscribe(
      (newArticles: Article[]) => {
        articles.push(...newArticles);
        this.articlesService.setArticlesAndNotify(articles);
      },
      (error) => {
        console.error('Error fetching more articles:', error);
      }
    );
  }
}
