import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {catchError, map, tap} from "rxjs/operators";
import {Observable, throwError} from "rxjs";
import {ArticleService} from "../articles/service/article.service";
import {NotificationService} from "../notifications/service/notification.service";
import {AuthorizationService} from "../auth/service/authorization.service";
import {Article} from "../articles/dto/article.type";
import {ArticleRequest} from "../articles/dto/article-request.type";
import {Notification} from "../notifications/dto/notification.type";
import {RegisterRequest} from "../auth/dto/register-request.type";
import {LoginRequest} from "../auth/dto/login-request.type";

@Injectable({providedIn: 'root' })
export class DataStorageService {
  private apiUrl = 'http://localhost:3000';

  constructor(
    private http: HttpClient,
    private articleService: ArticleService,
    private notificationService: NotificationService,
    private authorizationService: AuthorizationService,
  ) {
  }

  fetchArticles() {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/`;

    return this.http
      .get<Article[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(this.mapArticleData),
        tap(this.updateArticleService)
      );
  }

  updateArticle(id: string, request: ArticleRequest) {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/${id}`;
    console.log(request)
    return this.http
      .put<void>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(() => this.fetchArticles().subscribe())
      )
      .subscribe();
  }

  storeArticle(request: ArticleRequest) {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/`;
    console.log(request)
    return this.http
      .post<void>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(() => this.fetchArticles().subscribe())
      )
      .subscribe();
  }

  deleteArticle(articleId: string) {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/${articleId}`;

    return this.http
      .delete<void>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(() => this.articleService.deleteArticle(articleId))
      )
      .subscribe();
  }

  likeArticle(articleId: string) {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/like/${articleId}`;

    return this.http
      .post<any>(endpoint, null, {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(response => {
          const status: 'like' | 'dislike' = response?.status;
          this.articleService.likeOrDislikeArticle(articleId, status);
        })
      )
      .subscribe();
  }

  fetchNotifications() {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/notifications/api/`;

    return this.http
      .get<Notification[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(notifications => notifications.map(notification => ({
          ...notification
        }))),
        tap(notifications => this.notificationService.setNotifications(notifications))
      );
  }

  login(request: LoginRequest) {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/auth/api/login`;

    return this.http.post<any>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError((error) => {
          if (error.status === 404) {
            const errorMessage = "User does not exist!";
            this.authorizationService.handleLoginError(errorMessage);
          } else if (error.status === 401) {
            const errorMessage = "Invalid credentials!";
            this.authorizationService.handleLoginError(errorMessage);
          } else {
            console.error(error);
          }
          return throwError(error);
        }),
        tap(userData => this.authorizationService.handleLogin(userData))
      )
      .subscribe();
  }

  register(request: RegisterRequest) {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/auth/api/register`;

    return this.http.post<void>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError((error) => {
          if (error.status === 400) {
            const errorMessage = "Username already exist!";
            this.authorizationService.handleRegisterError(errorMessage);
          } else {
            console.error(error);
          }
          return throwError(error);
        }),
        tap(() => this.authorizationService.handleRegister())
      )
      .subscribe();
  }

  private createHeaders(): HttpHeaders {
    const userData = this.authorizationService.getUserData();

    if (userData) {
      const token = userData.token;

      return new HttpHeaders({
        Accept: 'application/json',
        'Content-Type': 'application/json',
        'Authorization': token
      });
    } else {
      return new HttpHeaders({
        Accept: 'application/json',
        'Content-Type': 'application/json'
      });
    }
  }

  private handleHttpError(error: HttpErrorResponse): Observable<never> {
    console.error(error);
    return throwError(error);
  }

  private mapArticleData = (articles: Article[]) =>
    articles.map(article => ({
      ...article,
      elapsed: this.getTimeElapsed(article.timestamp),
      numOfLikes: article.likes.users.length,
    }));

  private updateArticleService = (articles: Article[]) =>
    this.articleService.setArticles(articles);

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
