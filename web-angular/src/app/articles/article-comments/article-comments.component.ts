import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Article} from "../dto/article.type";
import {ArticleService} from "../service/article.service";

@Component({
  selector: 'author-activities',
  templateUrl: './article-comments.component.html',
  styleUrls: ['./article-comments.component.scss']
})
export class ArticleCommentsComponent implements OnInit {

  article!: Article;
  private articleSubscription: Subscription = new Subscription();

  constructor(
    private articleService: ArticleService
  ) {
  }

  ngOnInit(): void {
    this.articleSubscription = this.articleService.activityChanged
      .subscribe(
        (article: Article) => {
          this.article = article;
        }
      );
    this.article = this.articleService.getArticle();
  }
}
