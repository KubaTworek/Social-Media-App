import {Component, Input, ViewChild, ViewEncapsulation} from "@angular/core";
import {Article} from "../../dto/article.type";
import {DataStorageService} from "../../../shared/data-storage.service";
import {ArticleDetailsComponent} from "./article-details/article-details.component";
import {AuthorizationService} from "../../../auth/service/authorization.service";

@Component({
  selector: 'article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArticleCardComponent {
  @Input() article!: Article
  @ViewChild(ArticleDetailsComponent) articleDetailsComponent!: ArticleDetailsComponent;

  constructor(private dataStorage: DataStorageService, private authorizationService: AuthorizationService) {
  }

  openDetails(event: Event): void {
    const clickedElement = event.target as HTMLElement;

    const isLikeButton = clickedElement.classList.contains('article-card__like-button');
    const isFollowButton = clickedElement.classList.contains('article-card__follow-button');
    const isNameButton = clickedElement.classList.contains('article-card__author-name');

    if (!isLikeButton && !isFollowButton && !isNameButton) {
      this.articleDetailsComponent.open();
    }
  }

  likeArticle(articleId: string) {
    if (this.isUser()) {
      this.dataStorage.likeArticle(articleId);
    }
  }

  showLikes(articleId: string): void {
    if (this.article.likes.users.length > 0) {
      const userNames = this.article.likes.users.map((user: string) => `<div>${user}</div>`).join('');
      const tooltipContent = `<div class="article-card__like-tooltip-content">${userNames}</div>`;
      const tooltip = document.createElement('div');
      tooltip.classList.add('article-card__like-tooltip');
      tooltip.innerHTML = tooltipContent;
      const likeButton = document.querySelector(`#article-card__like-container-${articleId}`);
      likeButton?.appendChild(tooltip);
    }
  }

  hideLikes(articleId: string): void {
    const likeButton = document.querySelector(`#article-card__like-container-${articleId}`);
    const tooltip = likeButton?.querySelector('.article-card__like-tooltip');
    if (tooltip) {
      likeButton?.removeChild(tooltip);
    }
  }

  followAuthor(id: string) {
    this.dataStorage.followAuthor(id);
  }

  unfollowAuthor(id: string) {
    this.dataStorage.unfollowAuthor(id);
  }

  private isUser(): boolean {
    const role = this.authorizationService.getRole();

    return role == 'ROLE_USER';
  }
}
