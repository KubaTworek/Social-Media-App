import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class LikeService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkwMjEwNjE0LCJleHAiOjE2OTAyMjE0MTR9.2YoY-8VS_R83lbeaUqFUJtPbcCLpIfkeIwDVnJGZdNc';
  constructor(private http: HttpClient) {
  }

  async likeArticle(id: string): Promise<any> {
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': this.jwt
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
