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

  constructor(private dataStorage: DataStorageService) {
  }

  openModal(): void {
    this.isOpen = true;
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    const overlayModal = this.overlayModalRef.nativeElement as HTMLElement;
    deleteModal.style.display = 'block';
    overlayModal.style.display = 'block';
  }

  closeModal(): void {
    this.isOpen = false;
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    const overlayModal = this.overlayModalRef.nativeElement as HTMLElement;
    deleteModal.style.display = 'none';
    overlayModal.style.display = 'none';
  }

  isAbleToDelete() {
    const userDataJson = sessionStorage.getItem("userData");
    const username = userDataJson ? JSON.parse(userDataJson).username : null;

    return username !== null && username === this.article.author.username;
  }

  isAbleToEdit() {
    const userDataJson = sessionStorage.getItem("userData");
    const username = userDataJson ? JSON.parse(userDataJson).username : null;

    return username !== null && username === this.article.author.username;
  }

  deleteArticle(articleId: string) {
    this.dataStorage.deleteArticle(articleId);
  }

  likeArticle(articleId: string) {
    this.dataStorage.likeArticle(articleId);
  }

  showLikes(articleId: string) {
    this.displayLikeTooltip(articleId);
  }

  displayLikeTooltip(articleId: string): void {
    if (this.article.likes.users.length > 0) {
      const userNames = this.article.likes.users.map((user: string) => `<div>${user}</div>`).join('');
      const tooltipContent = `<div class="article-card__like-tooltip-content">${userNames}</div>`;
      const tooltip = document.createElement('div');
      tooltip.classList.add('article-card__like-tooltip');
      tooltip.innerHTML = tooltipContent;
      const likeButton = document.querySelector(`#like-container-${articleId}`);
      likeButton?.appendChild(tooltip);
    }
  }

  openDeleteModal(): void {
    this.articleDeleteComponent.openModal();
  }

  save() {
    const textareaElement = document.querySelector('.article-details__content') as HTMLTextAreaElement;

    if (textareaElement) {
      const request = new ArticleRequest(textareaElement.value)
      this.dataStorage.updateArticle(this.article.id, request)
    }
  }

  onArticleDeleteConfirmed(articleId: string): void {
    this.deleteArticle(articleId)
    this.articleDeleteComponent.closeModal();
  }

  onArticleDeleteCancelled(): void {
    this.articleDeleteComponent.closeModal();
  }

  hideLikeInfo(articleId: string): void {
    const likeButton = document.querySelector(`#like-container-${articleId}`);
    const tooltip = likeButton?.querySelector('.article-card__like-tooltip');
    if (tooltip) {
      likeButton?.removeChild(tooltip);
    }
  }
}
