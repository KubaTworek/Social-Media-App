import {Injectable} from '@angular/core';
import {Article} from '../dto/article.type';
import {Subject} from "rxjs";
import {Like} from "../dto/like.type";


@Injectable()
export class ArticleService {
  articlesChanged = new Subject<Article[]>();
  private articles: Article[] = [];

  constructor() {
  }

  setArticles(articles: Article[]) {
    this.articles = articles;
    this.articlesChanged.next(this.articles.slice());
  }

  getArticles() {
    return this.articles.slice();
  }

  getArticlesByKeyword(keyword: string) {
    return this.articles.filter(article => {
      return (
        article.text.toLowerCase().includes(keyword.toLowerCase())
      );
    });
  }

  deleteArticle(articleId: string) {
    const index = this.articles.findIndex(article => article.id === articleId);
    if (index !== -1) {
      this.articles.splice(index, 1);
      this.articlesChanged.next(this.articles.slice());
    }
  }

  likeArticle(articleId: string, status: string) {
    const article = this.articles.find(article => article.id === articleId);
    if (article) {
      if (status === 'like') {
        article.numOfLikes += 1;
      } else if (status === 'dislike') {
        article.numOfLikes -= 1;
      } else {
        console.error(`Unknown response: ${status}`);
      }
      this.articlesChanged.next(this.articles.slice());
    }
  }

  showLikes(articleId: string, users: string[]) {
    const article: Article | undefined = this.articles.find(article => article.id === articleId);
    if (article !== undefined) {
      for (const user of users) {
        if (!article.likes.some(like => like.username === user)) {
          article.likes.push(new Like(user));
        }
      }
      this.articlesChanged.next(this.articles.slice());
    }
  }
}
