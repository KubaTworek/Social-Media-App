import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Notification} from './dto/notification.type';
import {ArticleRequest} from "../articles/dto/article-request.type";

@Injectable()
export class NotificationService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkwMjEwNjE0LCJleHAiOjE2OTAyMjE0MTR9.2YoY-8VS_R83lbeaUqFUJtPbcCLpIfkeIwDVnJGZdNc';

  constructor(private http: HttpClient) {
  }

  async getNotifications(): Promise<any> {
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': this.jwt
    });

    try {
      return await this.http.get<Notification[]>('http://localhost:3000/notifications/api/', {headers}).toPromise();
    } catch (error) {
      console.error(error);
      return undefined;
    }
  }
}
