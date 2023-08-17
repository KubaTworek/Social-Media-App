import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ArticleService} from "../service/article.service";
import {Article} from "../dto/article.type";
import {catchError, tap, throwError} from "rxjs";
import {map} from "rxjs/operators";
import {ArticleRequest} from "../dto/article-request.type";

@Injectable({providedIn: 'root'})
export class DataStorageService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkyMjI4MDEzLCJleHAiOjE2OTIyMzg4MTN9.wVx6plcLMDsYx0vNaItNByY5fsI-fg7WlvKJzqwEi8E';

  constructor(
    private http: HttpClient,
    private articleService: ArticleService,
  ) {
  }

  fetchArticles() {
    const headers = this.createHeadersWithJwt();

    return this.http
      .get<Article[]>(`http://localhost:3000/articles/api/`, {
        headers,
      })
      .pipe(
        map(articles => {
          return articles.map(article => {
            return {
              ...article,
              elapsed: this.getTimeElapsed(article.timestamp),
              likes: [],
            };
          });
        }),
        tap(articles => {
          this.articleService.setArticles(articles);
        })
      )
  }

  storeArticle(request: ArticleRequest) {
    const headers = this.createHeadersWithJwt();

    return this.http
      .post<void>(
        'http://localhost:3000/articles/api/',
        JSON.stringify(request),
        {headers}
      )
      .pipe(
        catchError((error) => {
          console.error(error);
          return throwError(error);
        })
      )
      .subscribe(() => {
        this.fetchArticles().subscribe(() => {
          console.log(request);
        });
      });
  }

  deleteArticle(articleId: string) {
    const headers = this.createHeadersWithJwt();

    return this.http
      .delete<void>(
        `http://localhost:3000/articles/api/${articleId}`,
        {headers}
      )
      .pipe(
        catchError((error) => {
          console.error(error);
          return throwError(error);
        })
      )
      .subscribe(() => {
        this.articleService.deleteArticle(articleId)
      });
  }

  likeArticle(articleId: string) {
    const headers = this.createHeadersWithJwt();

    return this.http
      .post<any>(
        `http://localhost:3000/articles/api/like/${articleId}`,
        null,
        {headers}
      )
      .pipe(
        catchError((error) => {
          console.error(error);
          return throwError(error);
        })
      )
      .subscribe(response => {
        const status: string = response?.status
        this.articleService.likeArticle(articleId, status)
      });
  }

  showLikes(articleId: string) {
    const headers = this.createHeaders();

    return this.http
      .get<any>(
        `http://localhost:3000/articles/api/like/${articleId}`,
        {headers}
      )
      .pipe(
        catchError((error) => {
          console.error(error);
          return throwError(error);
        })
      )
      .subscribe(response => {
        const users: string[] = response?.users
        this.articleService.showLikes(articleId, users)
      })
  }

  private createHeadersWithJwt(): HttpHeaders {
    return new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
      Authorization: this.jwt
    });
  }

  private createHeaders(): HttpHeaders {
    return new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json'
    });
  }

  private getTimeElapsed(timestamp: Date): string {
    const now = new Date();
    const timeDiff = now.getTime() - new Date(timestamp).getTime();

    if (timeDiff < 60000) {
      const seconds = Math.floor(timeDiff / 1000);
      return seconds + "s";
    } else if (timeDiff < 3600000) {
      const minutes = Math.floor(timeDiff / 60000);
      return minutes + "m";
    } else if (timeDiff < 86400000) {
      const hours = Math.floor(timeDiff / 3600000);
      return hours + "h";
    } else if (timeDiff < 604800000) {
      const days = Math.floor(timeDiff / 86400000);
      return days + "d";
    } else {
      const weeks = Math.floor(timeDiff / 604800000);
      return weeks + "w";
    }
  }

}
