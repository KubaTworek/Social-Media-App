import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {ArticleService} from "./article.service";
import {ArticleDetails} from "../dto/article-details.type";

@Injectable({providedIn: 'root'})
export class ArticleResolverService implements Resolve<ArticleDetails> {
  constructor(
    private dataStorageService: DataStorageService,
    private articleService: ArticleService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): ArticleDetails {
    const articleId = route.paramMap.get('id');
    let article = this.articleService.getArticle();

    if (articleId && articleId !== article.id) {
      this.dataStorageService.fetchArticle(articleId)
    }
    return article;
  }
}
