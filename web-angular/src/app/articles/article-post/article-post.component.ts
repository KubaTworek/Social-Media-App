import {Component, Input} from "@angular/core";
import {ArticleRequest} from "../dto/article-request.type";
import {DataStorageService} from "../../shared/data-storage.service";

@Component({
  selector: 'article-post',
  templateUrl: './article-post.component.html',
  styleUrls: ['./article-post.component.scss']
})
export class ArticlePostComponent {
  @Input() placeholderText: string = '';
  @Input() motherArticleId: string | null = null;
  articleContent = '';

  constructor(private dataStorageService: DataStorageService) {
  }

  createArticle(): void {
    if (!this.articleContent) {
      return;
    }

    const request: ArticleRequest = {
      text: this.articleContent,
      articleMotherId: this.motherArticleId
    };

    this.dataStorageService.storeArticle(request);
    this.articleContent = '';
  }
}
