import {Component, OnDestroy, OnInit} from '@angular/core';
import {ArticleService} from '../service/article.service';
import {Article} from '../dto/article.type';
import {Subscription} from "rxjs";
import {SearchService} from "../../search-board/service/search.service";
import {DataStorageService} from "../../shared/data-storage.service";

@Component({
  selector: 'article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.scss']
})
export class ArticleListComponent implements OnInit, OnDestroy {
  articles: Article[] = [];
  keyword: string = '';
  isForYouActive = true;
  isFollowingActive = false;
  private keywordSubscription: Subscription = new Subscription();
  private articlesSubscription: Subscription = new Subscription();
  private activeStatusSubscription: Subscription = new Subscription();
  private currentForYouPage = 0;
  private currentFollowingPage = 0;

  constructor(
    private dataStorageService: DataStorageService,
    private articleService: ArticleService,
    private searchService: SearchService
  ) {
  }

  ngOnInit(): void {
    this.keywordSubscription = this.searchService.articleListUpdated$
      .subscribe((keyword: string) => {
          this.keyword = keyword;
          this.articles = this.articleService.getArticlesByKeyword(keyword);
        }
      );

    this.articlesSubscription = this.articleService.articlesChanged
      .subscribe(
        (articles: Article[]) => {
          this.articles = articles;
        }
      );

    this.activeStatusSubscription = this.articleService.isForYouActive$
      .subscribe((isForYouActive: boolean) => {
        this.isForYouActive = isForYouActive;
      });

    this.activeStatusSubscription = this.articleService.isFollowingActive$
      .subscribe((isFollowingActive: boolean) => {
        this.isFollowingActive = isFollowingActive;
      });
  }

  ngOnDestroy(): void {
    this.keywordSubscription.unsubscribe();
    this.articlesSubscription.unsubscribe();
  }

  loadMoreArticles() {
    if (this.isForYouActive) {
      this.currentFollowingPage = 0;
      this.currentForYouPage++;
      this.dataStorageService.fetchArticles(this.currentForYouPage, 5).subscribe(
        (newArticles: Article[]) => {
          this.articles.push(...newArticles);
          this.articleService.setArticles(this.articles)
        },
        (error) => {
          console.error('Error fetching more articles:', error);
        }
      );
    }
    if (this.isFollowingActive) {
      this.currentForYouPage = 0;
      this.currentFollowingPage++;
      this.dataStorageService.fetchFollowingArticles(this.currentFollowingPage, 5).subscribe(
        (newArticles: Article[]) => {
          this.articles.push(...newArticles);
          this.articleService.setArticles(this.articles)
        },
        (error) => {
          console.error('Error fetching more articles:', error);
        }
      );
    }
  }
}
