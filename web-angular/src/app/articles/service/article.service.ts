import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Article} from '../dto/article.type';
import {ArticleRequest} from "../dto/article-request.type";
import {catchError, Observable, throwError} from "rxjs";
import { map } from 'rxjs/operators';


@Injectable()
export class ArticleService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkwNDE3NTg2LCJleHAiOjE2OTA0MjgzODZ9.rHmzIO3leTyCKgxA9mDiutVNuIKa11glXcsx9Gi7gWI';

  constructor(private http: HttpClient) {
  }

  getArticles(keyword: string): Observable<Article[]> {
    const headers = this.createHeaders();

    return this.http.get<Article[]>(`http://localhost:3000/articles/api/${keyword}`, {
      headers,
    }).pipe(
      map((response) =>
        response.map((article) => ({
          ...article,
          elapsed: this.getTimeElapsed(article.timestamp),
        }))
      )
    ).pipe(
      catchError((error) => {
        console.error(error);
        return throwError(error);
      })
    );
  }

  postArticle(data: ArticleRequest): Observable<void> {
    const headers = this.createHeaders();

    return this.http.post<void>('http://localhost:3000/articles/api/', JSON.stringify(data), {
      headers,
    }).pipe(
      catchError((error) => {
        console.error(error);
        return throwError(error);
      })
    );
  }

  deleteArticle(articleId: string): Observable<void> {
    const headers = this.createHeaders();

    return this.http.delete<void>(`http://localhost:3000/articles/api/${articleId}`, {
      headers,
    }).pipe(
      catchError((error) => {
        console.error(error);
        return throwError(error);
      })
    );
  }

  private createHeaders(): HttpHeaders {
    return new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
      Authorization: this.jwt,
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
