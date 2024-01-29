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
import {RegisterRequest} from "../auth/shared/register-request.type";
import {LoginRequest} from "../auth/shared/login-request.type";
import {TranslateService} from "@ngx-translate/core";
import {AuthorDto} from "../articles/dto/author.type";
import {AuthorsService} from "../authors/service/authors.service";
import {Author} from "../authors/dto/author.type";
import {AuthorWithActivities} from "../authors/dto/author-with-activities.type";
import {ArticleWithComments} from "../articles/dto/article-with-comments.type";

@Injectable({providedIn: 'root'})
export class DataStorageService {
  private apiUrl = 'http://localhost:3000';

  constructor(
    private http: HttpClient,
    private articleService: ArticleService,
    private notificationService: NotificationService,
    private authorsService: AuthorsService,
    private authorizationService: AuthorizationService,
    private translateService: TranslateService,
  ) {
  }

  fetchAuthors(): Observable<AuthorDto[]> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/authors/api/`;
    console.log(endpoint)
    return this.http
      .get<AuthorDto[]>(endpoint, {headers})
      .pipe(catchError(this.handleHttpError));
  }

  updateNotification(notificationId: string, authorId: string): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/notifications/api/${notificationId}/author/${authorId}`;

    this.http
      .put<void>(endpoint, null, {headers})
      .pipe(catchError(this.handleHttpError))
      .subscribe();
  }

  fetchArticles(page: number, size: number): Observable<Article[]> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/?page=${page}&size=${size}`;
    console.log(endpoint)

    return this.http
      .get<Article[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(this.mapArticleData)
      );
  }

  fetchArticle(articleId: string): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/id/external/${articleId}`;
    console.log(endpoint)
    this.http
      .get<ArticleWithComments>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(this.mapArticle),
        tap(article => this.articleService.setArticle(article))
      )
      .subscribe();
  }

  fetchFollowingArticles(page: number, size: number): Observable<Article[]> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/following?page=${page}&size=${size}`;
    console.log(endpoint)
    return this.http
      .get<Article[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(this.mapArticleData)
      );
  }

  updateArticle(id: string, request: ArticleRequest): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/${id}`;
    console.log(request)
    this.http
      .put<void>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(() => this.articleService.updateArticle(id, request.text))
      )
      .subscribe();
  }

  followAuthor(id: string): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/authors/api/follow/${id}`;

    this.http
      .put<void>(endpoint, null, {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(() => {
          this.articleService.followAuthor(id)
          this.authorizationService.followAuthor()
        })
      )
      .subscribe();
  }

  unfollowAuthor(id: string): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/authors/api/unfollow/${id}`;

    this.http
      .put<void>(endpoint, null, {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(() => {
          this.articleService.unfollowAuthor(id)
          this.authorizationService.unfollowAuthor()
        })
      )
      .subscribe();
  }

  storeArticle(request: ArticleRequest): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/`;

    this.http
      .post<void>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(() => this.fetchArticles(0, 5)
          .pipe(
            tap(this.updateArticleService)
          ).subscribe())
      )
      .subscribe();
  }

  deleteArticle(articleId: string): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/${articleId}`;

    this.http
      .delete<void>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(() => this.articleService.deleteArticle(articleId))
      )
      .subscribe();
  }

  likeArticle(articleId: string): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/articles/api/like/${articleId}`;

    this.http
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

  fetchFollowingAuthors(author: Author): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/authors/api/following/${author.id}`;

    this.http
      .get<Author[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(authors => authors.map(author => ({...author}))),
        tap(authors => this.authorsService.setAuthors(authors, author, 'following'))
      )
      .subscribe();
  }

  fetchFollowersAuthors(author: Author): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/authors/api/followers/${author.id}`;

    this.http
      .get<Author[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(authors => authors.map(author => ({...author}))),
        tap(authors => this.authorsService.setAuthors(authors, author, 'followers'))
      )
      .subscribe();
  }

  fetchAuthorActivities(authorId: string): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/notifications/api/${authorId}`;

    this.http
      .get<AuthorWithActivities>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        tap(author => this.authorsService.setAuthorWithActivities(author))
      )
      .subscribe();
  }

  fetchNotifications(): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/notifications/api/`;

    this.http
      .get<Notification[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(notifications => notifications.map(notification => ({...notification}))),
        tap(notifications => this.notificationService.setNotifications(notifications))
      )
      .subscribe();
  }

  fetchNotificationsAdmin(): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/notifications/api/admin`;

    this.http
      .get<Notification[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
        map(notifications => notifications.map(notification => ({...notification}))),
        tap(notifications => this.notificationService.setNotifications(notifications))
      )
      .subscribe();
  }

  login(request: LoginRequest): Observable<any> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/auth/api/login`;

    return this.http.post<any>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError((error) => {
          if (error.status === 404) {
            const errorMessage = this.translateService.instant('USERNAME_NOT_EXISTS');
            this.authorizationService.handleLoginError(errorMessage);
          } else if (error.status === 401) {
            const errorMessage = this.translateService.instant('INVALID_CREDENTIALS');
            this.authorizationService.handleLoginError(errorMessage);
          } else {
            console.error(error);
          }
          return throwError(error);
        }),
        tap(userData => this.authorizationService.handleLogin(userData))
      );
  }

  refreshToken(): void {
    const headers = this.createHeadersForRefresh()
    const endpoint = `${this.apiUrl}/auth/api/refresh-token`;

    this.http.post<any>(endpoint, null, {headers})
      .pipe(
        catchError((error) => {
          console.log('csc')
          return throwError(error);
        }),
        tap(userData => {
          this.authorizationService.handleRefresh(userData)
        })
      )
      .subscribe();
  }

  register(request: RegisterRequest): Observable<void> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/auth/api/register`;

    return this.http.post<void>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError((error) => {
          if (error.status === 400) {
            const errorMessage = this.translateService.instant('USERNAME_ALREADY_EXISTS');
            this.authorizationService.handleRegisterError(errorMessage);
          } else {
            console.error(error);
          }
          return throwError(error);
        }),
        tap(() => this.authorizationService.handleRegister())
      );
  }

  updateArticleService = (articles: Article[]) =>
    this.articleService.setArticlesAndNotify(articles);

  private createHeaders(): HttpHeaders {
    const token = this.authorizationService.getToken();

    return new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
      'Authorization': token || ''
    });
  }

  private createHeadersForRefresh(): HttpHeaders {
    const token = this.authorizationService.getRefreshToken()

    return new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
      'Authorization': token || ''
    });
  }

  private handleHttpError(error: HttpErrorResponse): Observable<never> {
    console.error(error);
    return throwError(error);
  }

  private mapArticleData = (articles: Article[]): Article[] =>
    articles.map(article => ({
      ...article,
      elapsed: this.getTimeElapsed(article.timestamp),
      createDate: this.formatDateTime(article.timestamp),
      numOfLikes: article.likes.users.length,
      numOfComments: article.numOfComments
    }));

  private mapArticle = (article: ArticleWithComments): ArticleWithComments => ({
    ...article,
    elapsed: this.getTimeElapsed(article.timestamp),
    createDate: this.formatDateTime(article.timestamp),
    numOfLikes: article.likes.users.length,
    numOfComments: article.comments.length,
    comments: article.comments.map(comment => this.mapArticle(comment))
  });

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

  private formatDateTime(inputDateString: Date): string {
    const date = new Date(inputDateString);

    const hour = date.getHours();
    const minute = date.getMinutes();
    const day = date.getDate();
    const month = date.toLocaleString('en-US', {month: 'short'});
    const year = date.getFullYear();

    const formattedTime = this.formatTime(hour, minute);
    const formattedDate = `${month} ${day}, ${year}`;

    return `${formattedTime} · ${formattedDate}`;
  }

  private formatTime(hour: number, minute: number): string {
    const isPM = hour >= 12;
    const formattedHour = hour % 12 || 12;
    const period = isPM ? 'PM' : 'AM';

    return `${formattedHour}:${minute.toString().padStart(2, '0')} ${period}`;
  }
}
