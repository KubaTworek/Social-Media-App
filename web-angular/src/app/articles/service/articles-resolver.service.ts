import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Article} from "../dto/article.type";
import {DataStorageService} from "../../shared/data-storage.service";
import {ArticleService} from "./article.service";
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class ArticlesResolverService implements Resolve<Article[]> {
  constructor(
    private dataStorageService: DataStorageService,
    private articlesService: ArticleService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Article[]> | Article[] {
    const articles = this.articlesService.getArticles();

    if (articles.length === 0) {
      return this.dataStorageService.fetchArticles();
    } else {
      return articles;
    }
  }
}
