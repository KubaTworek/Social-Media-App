export class ArticleCreateRequest {
  public text: string;
  public articleMotherId: string | null;

  constructor(text: string, articleMotherId: string | null = null) {
    this.text = text;
    this.articleMotherId = articleMotherId;
  }
}
