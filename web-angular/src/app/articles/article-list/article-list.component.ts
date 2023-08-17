import {Component, OnDestroy, OnInit} from '@angular/core';
import {ArticleService} from '../service/article.service';
import {Article} from '../dto/article.type';
import {Subscription} from "rxjs";
import {SearchService} from "../../search-board/service/search.service";

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

  constructor(
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
    this.articles = this.articleService.getArticles();
  }

  ngOnDestroy(): void {
    this.keywordSubscription.unsubscribe();
    this.articlesSubscription.unsubscribe();
  }
}
