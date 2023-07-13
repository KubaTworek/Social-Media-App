import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class LikeService {
  constructor(private http: HttpClient) {
  }

  async likeArticle(id: string): Promise<any> {
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjg5MjgwMDA5LCJleHAiOjE2ODkyOTA4MDl9.nfFtuQCjKXE7W9gkzrXKVffkmwSlgks6uG0v-Obkkbk' || ''
    });

    try {
      return await this.http.post<any>(`http://localhost:3000/articles/api/like/${id}`, null, {headers}).toPromise();
    } catch (error) {
      console.error(error);
    }
  }

  async showLikes(id: string): Promise<any> {
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });

    try {
      return await this.http.get<any>(`http://localhost:3000/articles/api/like/${id}`, {headers}).toPromise();
    } catch (error) {
      console.error(error);
    }
  }
}
