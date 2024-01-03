import {Component, OnDestroy, OnInit} from '@angular/core';
import {ArticleService} from '../service/article.service';
import {Article} from '../dto/article.type';
import {Subscription} from 'rxjs';
import {SearchService} from '../../search-board/service/search.service';
import {DataStorageService} from '../../shared/data-storage.service';

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
  private subscriptions: Subscription[] = [];
  private currentForYouPage = 0;
  private currentFollowingPage = 0;

  constructor(
    private dataStorageService: DataStorageService,
    private articleService: ArticleService,
    private searchService: SearchService
  ) {
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.searchService.articleListUpdated$.subscribe((keyword: string) => {
        this.keyword = keyword;
        this.articles = this.articleService.getArticlesByKeyword(keyword);
      }),
      this.articleService.articlesChanged.subscribe((articles: Article[]) => {
        this.articles = articles;
      }),
      this.articleService.isForYouActive$.subscribe((isForYouActive: boolean) => {
        this.isForYouActive = isForYouActive;
      }),
      this.articleService.isFollowingActive$.subscribe((isFollowingActive: boolean) => {
        this.isFollowingActive = isFollowingActive;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  loadMoreArticles(): void {
    const fetchMethod = this.isForYouActive
      ? this.dataStorageService.fetchArticles.bind(this.dataStorageService)
      : this.dataStorageService.fetchFollowingArticles.bind(this.dataStorageService);

    const nextPage = this.isForYouActive ? ++this.currentForYouPage : ++this.currentFollowingPage;

    fetchMethod(nextPage, 5).subscribe(
      (newArticles: Article[]) => {
        this.articles.push(...newArticles);
        this.articleService.setArticles(this.articles);
      },
      (error) => {
        console.error('Error fetching more articles:', error);
      }
    );
  }
}
