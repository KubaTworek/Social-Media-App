import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ArticleService} from "../articles/service/article.service";
import {Article} from "../articles/dto/article.type";
import {catchError, tap, throwError} from "rxjs";
import {map} from "rxjs/operators";
import {ArticleRequest} from "../articles/dto/article-request.type";
import {Notification} from "../notifications/dto/notification.type";
import {NotificationService} from "../notifications/service/notification.service";
import {RegisterRequest} from "../auth/dto/register-request.type";
import {LoginRequest} from "../auth/dto/login-request.type";
import {AuthorizationService} from "../auth/service/authorization.service";

@Injectable({providedIn: 'root'})
export class DataStorageService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkyMzExODA1LCJleHAiOjE2OTIzMjI2MDV9.qtrF8LF1aXIFd-H5MroVNvvjfkVdrieOm2cEAMb5Dd0';

  constructor(
    private http: HttpClient,
    private articleService: ArticleService,
    private notificationService: NotificationService,
    private authorizationService: AuthorizationService,
  ) {
  }

  fetchArticles() {
    const headers = this.createHeaderWithJwt();

    return this.http
      .get<Article[]>(`http://localhost:3000/articles/api/`,
        {headers}
      )
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
    const headers = this.createHeaderWithJwt();

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
    const headers = this.createHeaderWithJwt();

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
    const headers = this.createHeaderWithJwt();

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
    const headers = this.createHeader();

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

  fetchNotifications() {
    const headers = this.createHeaderWithJwt();

    return this.http
      .get<Notification[]>(`http://localhost:3000/notifications/api/`,
        {headers}
      )
      .pipe(
        map(notifications => {
          return notifications.map(notification => {
            return {
              ...notification
            };
          });
        }),
        tap(notifications => {
          this.notificationService.setNotifications(notifications);
        })
      )
  }

  login(request: LoginRequest) {
    const headers = this.createHeaderWithJwt();

    return this.http
      .post<any>(`http://localhost:3000/auth/api/login`,
        JSON.stringify(request),
        {headers}
      )
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
        })
      )
      .subscribe(response => {
        const jwt = response?.jwt
        this.authorizationService.handleLogin(jwt)
      });
  }

  register(request: RegisterRequest) {
    const headers = this.createHeaderWithJwt();

    return this.http
      .post<void>(`http://localhost:3000/auth/api/register`,
        JSON.stringify(request),
        {headers}
      )
      .pipe(
        catchError((error) => {
          if (error.status === 400) {
            const errorMessage = "Username already exist!";
            this.authorizationService.handleRegisterError(errorMessage);
          } else {
            console.error(error);
          }
          return throwError(error);
        })
      )
      .subscribe(() => {
        this.authorizationService.handleRegister()
      });
  }

  private createHeaderWithJwt(): HttpHeaders {
    const jwtToken = sessionStorage.getItem('jwt');

    if (jwtToken) {
      return new HttpHeaders({
        Accept: 'application/json',
        'Content-Type': 'application/json',
        Authorization: jwtToken
      });
    } else {
      return new HttpHeaders({
        Accept: 'application/json',
        'Content-Type': 'application/json'
      });
    }
  }

  private createHeader(): HttpHeaders {
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
