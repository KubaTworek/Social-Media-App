import {Component} from "@angular/core";
import {ArticleService} from "../article.service";
import {ArticleRequest} from "../dto/article-request.type";

@Component({
  selector: 'article-post',
  templateUrl: './article-post.component.html',
  styleUrls: ['./article-post.component.scss']
})
export class ArticlePostComponent {
  articleContent = '';

  constructor(private articleService: ArticleService) {
  }

  createArticle(): void {
    const data: ArticleRequest = {
      title: '',
      text: this.articleContent
    };
    this.articleService.postArticle(data)
      .then(() => location.reload());
  }
}
