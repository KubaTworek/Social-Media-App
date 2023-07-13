import {Component, OnInit} from '@angular/core';
import {ArticleService} from '../article.service';
import {Article} from '../dto/article.type';

@Component({
  selector: 'article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.scss']
})
export class ArticleListComponent implements OnInit {
  articleList: Article[] = [];

  constructor(private articleService: ArticleService) {
  }

  ngOnInit(): void {
    this.getArticles();
  }

  async getArticles(): Promise<void> {
    try {
      this.articleList = await this.articleService.getArticles() || [];
      console.log(this.articleList);
    } catch (error) {
      console.error(error);
    }
  }
}
