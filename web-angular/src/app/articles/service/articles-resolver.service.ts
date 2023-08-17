import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Article} from "../dto/article.type";
import {DataStorageService} from "../shared/data-storage.service";
import {ArticleService} from "./article.service";

@Injectable({providedIn: 'root'})
export class ArticlesResolverService implements Resolve<Article[]> {
  constructor(
    private dataStorageService: DataStorageService,
    private articlesService: ArticleService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const recipes = this.articlesService.getArticles();

    if (recipes.length === 0) {
      return this.dataStorageService.fetchArticles();
    } else {
      return recipes;
    }
  }
}
