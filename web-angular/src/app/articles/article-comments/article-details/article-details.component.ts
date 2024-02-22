import {Component, ElementRef, EventEmitter, Input, Output, ViewChild, ViewEncapsulation} from "@angular/core";
import {ArticleDeleteComponent} from "./article-delete/article-delete.component";
import {DataStorageService} from "../../../shared/data-storage.service";
import {AuthorizationService} from "../../../auth/service/authorization.service";
import {ArticleUpdateRequest} from "../../dto/article-update.type";
import {ArticleDetails} from "../../dto/article-details.type";

@Component({
  selector: 'article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArticleDetailsComponent {

  @ViewChild('modal') deleteModalRef!: ElementRef;
  @ViewChild('overlay') overlayModalRef!: ElementRef;
  @Input() article!: ArticleDetails;
  @ViewChild(ArticleDeleteComponent) articleDeleteComponent!: ArticleDeleteComponent;
  @Output() saved = new EventEmitter<void>();

  isOpen: boolean = true;
  showOptions: boolean = false;

  constructor(
    private dataStorage: DataStorageService,
    private authorizationService: AuthorizationService
  ) {
  }

  canModify(): boolean {
    const username = this.authorizationService.getUsername();
    const role = this.authorizationService.getRole();

    return (username !== null && username === this.article.author.username) || role == 'ROLE_ADMIN';
  }

  getUsername(): string | null {
    return this.authorizationService.getUsername();
  }

  openDeleteModal(): void {
    this.articleDeleteComponent.open();
  }

  openOptionsList(): void {
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

  save(): void {
    const textareaElement = document.querySelector(`#content-${this.article.id}`) as HTMLTextAreaElement;

    if (textareaElement) {
      const request = new ArticleUpdateRequest(textareaElement.value, this.article.id);
      this.dataStorage.updateArticle(request);
    }
  }

  deleteArticle(articleId: string): void {
    this.dataStorage.deleteArticle(articleId);
  }

  likeArticle(articleId: string): void {
    if (this.isUser()) {
      this.dataStorage.likeArticle(articleId);
    }
  }

  showLikes(articleId: string): void {
    if (this.article.likes.users.length > 0) {
      const userNames = this.article.likes.users.map(user => `<div>${user}</div>`).join('');
      const tooltipContent = `<div class="article-details__like-tooltip-content">${userNames}</div>`;
      const tooltip = document.createElement('div');
      tooltip.classList.add('article-details__like-tooltip');
      tooltip.innerHTML = tooltipContent;
      const likeButton = document.querySelector(`#article-details__like-container-${articleId}`);
      likeButton?.appendChild(tooltip);
    }
  }

  hideLikes(articleId: string): void {
    const likeButton = document.querySelector(`#article-details__like-container-${articleId}`);
    const tooltip = likeButton?.querySelector('.article-details__like-tooltip');
    if (tooltip) {
      likeButton?.removeChild(tooltip);
    }
  }

  followOrUnfollowAuthor(id: string): void {
    if (this.article.author.isFollowed) {
      this.dataStorage.unfollowAuthor(id);
    } else {
      this.dataStorage.followAuthor(id);
    }
  }

  onArticleDeleteConfirmed(articleId: string): void {
    this.deleteArticle(articleId);
    this.articleDeleteComponent.close();
  }

  onArticleDeleteCancelled(): void {
    this.articleDeleteComponent.close();
  }

  private isUser(): boolean {
    const role = this.authorizationService.getRole();
    return role == 'ROLE_USER';
  }
}
