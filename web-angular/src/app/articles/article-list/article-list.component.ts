import {Component, OnInit} from '@angular/core';
import {ArticleService} from '../article.service';
import {Article} from '../dto/article.type';
import {Subscription} from "rxjs";
import {AppService} from "../../app.service";

@Component({
  selector: 'article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.scss']
})
export class ArticleListComponent implements OnInit {
  articleList: Article[] = [];
  private subscription: Subscription = new Subscription();

  constructor(
    private articleService: ArticleService,
    private appService: AppService
  ) {
  }

  ngOnInit(): void {
    this.subscription = this.appService.articleListUpdated$.subscribe(
      (keyword: string) => {
        this.getArticles(keyword);
      }
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  async getArticles(keyword: string): Promise<void> {
    try {
      this.articleList = await this.articleService.getArticles(keyword) || [];
    } catch (error) {
      console.error(error);
    }
  }
}
