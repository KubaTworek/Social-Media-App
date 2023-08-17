import {Component} from "@angular/core";
import {ArticleRequest} from "../dto/article-request.type";
import {DataStorageService} from "../../shared/data-storage.service";

@Component({
  selector: 'article-post',
  templateUrl: './article-post.component.html',
  styleUrls: ['./article-post.component.scss']
})
export class ArticlePostComponent {
  articleContent = '';

  constructor(private dataStorageService: DataStorageService) {
  }

  createArticle(): void {
    const request: ArticleRequest = {
      title: '',
      text: this.articleContent
    };
    this.articleContent = '';
    this.dataStorageService.storeArticle(request);
  }
}
