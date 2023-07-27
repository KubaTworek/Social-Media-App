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
  articleList: Article[] = [];
  private subscription: Subscription = new Subscription();

  constructor(
    private articleService: ArticleService,
    private searchService: SearchService
  ) {
  }

  ngOnInit(): void {
    this.subscription = this.searchService.articleListUpdated$
      .subscribe((keyword: string) => {
        this.getArticles(keyword);
      }
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  async getArticles(keyword: string): Promise<void> {
    try {
      this.articleService.getArticles(keyword).subscribe(
        (articles: Article[]) => {
          this.articleList = articles || [];
        },
        (error) => {
          console.error(error);
        }
      );
    } catch (error) {
      console.error(error);
    }
  }
}
