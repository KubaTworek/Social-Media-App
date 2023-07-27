import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable} from "rxjs";

@Injectable()
export class LikeService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkwNDE3NTg2LCJleHAiOjE2OTA0MjgzODZ9.rHmzIO3leTyCKgxA9mDiutVNuIKa11glXcsx9Gi7gWI';

  constructor(private http: HttpClient) {
  }

  likeArticle(id: string): Observable<any> {
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': this.jwt
    });

    return this.http.post<any>(`http://localhost:3000/articles/api/like/${id}`, null, { headers })
      .pipe(
        catchError(error => {
          console.error(error);
          return [];
        })
      );
  }

  showLikes(id: string): Observable<any> {
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });

    return this.http.get<any>(`http://localhost:3000/articles/api/like/${id}`, { headers })
      .pipe(
        catchError(error => {
          console.error(error);
          return [];
        })
      );
  }
}
