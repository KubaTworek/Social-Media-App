import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Article} from '../dto/article.type';
import {ArticleRequest} from "../dto/article-request.type";

@Injectable()
export class ArticleService {
  private jwt = 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5IiwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNjkwNDA1MzkyLCJleHAiOjE2OTA0MTYxOTJ9.eN_b2gNR3B1MNfuAZMnEGK566yMpafJHfgIY1fmUj2g';

  constructor(private http: HttpClient) {
  }

  async getArticles(keyword: string): Promise<Article[] | undefined> {
    try {
      const response = await this.http.get<Article[]>(`http://localhost:3000/articles/api/${keyword}`).toPromise();
      return response?.map(article => ({
        ...article,
        elapsed: this.getTimeElapsed(article.timestamp)
      }));
    } catch (error) {
      console.error(error);
      return undefined;
    }
  }

  async postArticle(data: ArticleRequest): Promise<void> {
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': this.jwt
    });

    try {
      await this.http.post('http://localhost:3000/articles/api/', JSON.stringify(data), {headers}).toPromise();
    } catch (error) {
      console.error(error);
    }
  }

  async deleteArticle(articleId: string): Promise<void> {
    const headers = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': this.jwt
    });

    try {
      await this.http.delete(`http://localhost:3000/articles/api/${articleId}`, {headers}).toPromise();
    } catch (error) {
      console.error(error);
    }
  }

  getTimeElapsed(timestamp: Date): string {
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