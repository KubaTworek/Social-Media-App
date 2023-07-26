import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Notification} from '../dto/notification.type';

@Injectable()
export class NotificationService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkwNDA1MzkyLCJleHAiOjE2OTA0MTYxOTJ9.eN_b2gNR3B1MNfuAZMnEGK566yMpafJHfgIY1fmUj2g';

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
