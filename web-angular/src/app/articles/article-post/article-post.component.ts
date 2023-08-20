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
    if (!this.articleContent) {
      return;
    }

    const request: ArticleRequest = {
      title: '', // Set appropriate title
      text: this.articleContent
    };

    this.dataStorageService.storeArticle(request);
    this.articleContent = '';
  }
}
