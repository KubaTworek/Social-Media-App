import {Component, ElementRef, EventEmitter, Input, Output, ViewChild, ViewEncapsulation} from "@angular/core";
import {Article} from "../../../dto/article.type";
import {ArticleDeleteComponent} from "../article-delete/article-delete.component";
import {DataStorageService} from "../../../../shared/data-storage.service";
import {ArticleRequest} from "../../../dto/article-request.type";

@Component({
  selector: 'article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArticleDetailsComponent {

  @ViewChild('modal') deleteModalRef!: ElementRef;
  @ViewChild('overlay') overlayModalRef!: ElementRef;
  @Input() article!: Article
  @ViewChild(ArticleDeleteComponent) articleDeleteComponent!: ArticleDeleteComponent;
  @Output() saved = new EventEmitter<void>();

  isOpen: boolean = false;
  showOptions: boolean = false;

  constructor(private dataStorage: DataStorageService) {
  }

  canModify() {
    const userDataJson = sessionStorage.getItem("userData");
    const username = userDataJson ? JSON.parse(userDataJson).username : null;

    return username !== null && username === this.article.author.username;
  }

  open(): void {
    this.isOpen = true;
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    const overlayModal = this.overlayModalRef.nativeElement as HTMLElement;
    deleteModal.style.display = 'block';
    overlayModal.style.display = 'block';
  }

  openDeleteModal(): void {
    this.articleDeleteComponent.open();
  }

  openOptionsList() {
    this.showOptions = !this.showOptions;
  }

  close(): void {
    this.isOpen = false;
    this.showOptions = false;
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    const overlayModal = this.overlayModalRef.nativeElement as HTMLElement;
    deleteModal.style.display = 'none';
    overlayModal.style.display = 'none';
  }

  save() {
    const textareaElement = document.querySelector(`#content-${this.article.id}`) as HTMLTextAreaElement;

    if (textareaElement) {
      const request = new ArticleRequest(textareaElement.value)
      this.dataStorage.updateArticle(this.article.id, request)
    }
  }

  deleteArticle(articleId: string) {
    this.dataStorage.deleteArticle(articleId);
  }

  likeArticle(articleId: string) {
    this.dataStorage.likeArticle(articleId);
  }

  showLikes(articleId: string): void {
    if (this.article.likes.users.length > 0) {
      const userNames = this.article.likes.users.map((user: string) => `<div>${user}</div>`).join('');
      const tooltipContent = `<div class="article-details__like-tooltip-content">${userNames}</div>`;
      const tooltip = document.createElement('div');
      tooltip.classList.add('article-card__like-tooltip');
      tooltip.innerHTML = tooltipContent;
      const likeButton = document.querySelector(`#article-details__like-container-${articleId}`);
      likeButton?.appendChild(tooltip);
    }
  }

  hideLikes(articleId: string): void {
    const likeButton = document.querySelector(`#article-details__like-container-${articleId}`);
    const tooltip = likeButton?.querySelector('.article-card__like-tooltip');
    if (tooltip) {
      likeButton?.removeChild(tooltip);
    }
  }

  onArticleDeleteConfirmed(articleId: string): void {
    this.deleteArticle(articleId)
    this.articleDeleteComponent.close();
  }

  onArticleDeleteCancelled(): void {
    this.articleDeleteComponent.close();
  }
}
