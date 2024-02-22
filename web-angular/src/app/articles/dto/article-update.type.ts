export class ArticleUpdateRequest {
  public text: string;
  public articleId: string;

  constructor(text: string, articleId: string) {
    this.text = text;
    this.articleId = articleId;
  }
}
