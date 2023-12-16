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
  private keywordSubscription: Subscription = new Subscription();
  private articlesSubscription: Subscription = new Subscription();
  private currentPage = 0;

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
  }

  ngOnDestroy(): void {
    this.keywordSubscription.unsubscribe();
    this.articlesSubscription.unsubscribe();
  }

  loadMoreArticles() {
    this.currentPage++;
    this.dataStorageService.fetchArticles(this.currentPage, 5).subscribe(
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
