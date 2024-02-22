import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ArticleService} from "../service/article.service";
import {ArticleDetails} from "../dto/article-details.type";

@Component({
  selector: 'author-activities',
  templateUrl: './article-comments.component.html',
  styleUrls: ['./article-comments.component.scss']
})
export class ArticleCommentsComponent implements OnInit {

  article!: ArticleDetails;
  private articleSubscription: Subscription = new Subscription();

  constructor(
    private articleService: ArticleService
  ) {
  }

  ngOnInit(): void {
    this.articleSubscription = this.articleService.activityChanged
      .subscribe(
        (article: ArticleDetails) => {
          this.article = article;
        }
      );
    this.article = this.articleService.getArticle();
  }
}
