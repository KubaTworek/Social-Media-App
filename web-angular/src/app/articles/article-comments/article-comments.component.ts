import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Article} from "../dto/article.type";
import {ArticleService} from "../service/article.service";
import {ArticleWithComments} from "../dto/article-with-comments.type";

@Component({
  selector: 'author-activities',
  templateUrl: './article-comments.component.html',
  styleUrls: ['./article-comments.component.scss']
})
export class ArticleCommentsComponent implements OnInit {

  article!: ArticleWithComments;
  private articleSubscription: Subscription = new Subscription();

  constructor(
    private articleService: ArticleService
  ) {
  }

  ngOnInit(): void {
    this.articleSubscription = this.articleService.activityChanged
      .subscribe(
        (article: ArticleWithComments) => {
          this.article = article;
        }
      );
    this.article = this.articleService.getArticle();
  }
}
