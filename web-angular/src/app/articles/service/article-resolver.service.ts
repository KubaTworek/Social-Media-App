import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {Article} from "../dto/article.type";
import {ArticleService} from "./article.service";

@Injectable({providedIn: 'root'})
export class ArticleResolverService implements Resolve<Article> {
  constructor(
    private dataStorageService: DataStorageService,
    private articleService: ArticleService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Article {
    const articleId = route.paramMap.get('id');
    let article = this.articleService.getArticle();

    if (articleId && articleId !== article.id) {
      this.dataStorageService.fetchArticle(articleId)
    }

    return article;
  }
}
