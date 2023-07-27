import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Notification} from '../dto/notification.type';
import {catchError, Observable, throwError} from "rxjs";


@Injectable()
export class NotificationService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkwNDE3NTg2LCJleHAiOjE2OTA0MjgzODZ9.rHmzIO3leTyCKgxA9mDiutVNuIKa11glXcsx9Gi7gWI';

  constructor(private http: HttpClient) {
  }

  getNotifications(): Observable<Notification[]> {
    const headers = this.createHeaders();

    return this.http.get<Notification[]>('http://localhost:3000/notifications/api/', {
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
}
