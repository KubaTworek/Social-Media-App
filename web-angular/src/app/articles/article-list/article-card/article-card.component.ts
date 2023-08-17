import {Component, Input, ViewChild, ViewEncapsulation} from "@angular/core";
import {Article} from "../../dto/article.type";
import {ArticleDeleteComponent} from "./article-delete/article-delete.component";
import {DataStorageService} from "../../../shared/data-storage.service";
import {Like} from "../../dto/like.type";

@Component({
  selector: 'article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArticleCardComponent {
  @Input() article!: Article
  @ViewChild(ArticleDeleteComponent) articleDeleteComponent!: ArticleDeleteComponent;

  constructor(private dataStorage: DataStorageService) {
  }

  deleteArticle(articleId: string) {
    this.dataStorage.deleteArticle(articleId);
  }

  likeArticle(articleId: string) {
    this.dataStorage.likeArticle(articleId);
  }

  showLikes(articleId: string) {
    this.dataStorage.showLikes(articleId);
    this.displayLikeTooltip(articleId);
  }

  displayLikeTooltip(articleId: string): void {
    if (this.article.likes.length > 0) {
      const userNames = this.article.likes.map((like: Like) => `<div>${like.username}</div>`).join('');
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

  private addLike(articleId: string): void {
    const numberOfLikes = document.querySelector(`#likes-${articleId}`) as HTMLElement;
    const likeContainer = document.querySelector(`#like-container-${articleId}`) as HTMLElement;
    const currentLikes = parseInt(numberOfLikes.innerText);
    numberOfLikes.innerText = (currentLikes + 1).toString();
    likeContainer.style.filter = 'grayscale(0%)';
    likeContainer.style.color = 'red';
  }

  private deleteLike(articleId: string): void {
    const numberOfLikes = document.querySelector(`#likes-${articleId}`) as HTMLElement;
    const likeContainer = document.querySelector(`#like-container-${articleId}`) as HTMLElement;
    const currentLikes = parseInt(numberOfLikes.innerText);
    numberOfLikes.innerText = (currentLikes - 1).toString();
    likeContainer.style.filter = 'grayscale(100%)';
    likeContainer.style.color = '#555';
  }
}
