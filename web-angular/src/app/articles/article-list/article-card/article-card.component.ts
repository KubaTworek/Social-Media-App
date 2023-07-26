import {Component, Input, ViewChild, ViewEncapsulation} from "@angular/core";
import {Article} from "../../dto/article.type";
import {LikeService} from "../../service/like.service";
import {ArticleDeleteComponent} from "./article-delete/article-delete.component";
import {ArticleService} from "../../service/article.service";

@Component({
  selector: 'article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArticleCardComponent {
  @Input() article!: Article

  @ViewChild(ArticleDeleteComponent) articleDeleteComponent!: ArticleDeleteComponent;

  constructor(private likeService: LikeService, private articleService: ArticleService) {
  }

  deleteArticle(articleId: string) {
    this.articleService.deleteArticle(articleId)
      .then(() => location.reload());
  }

  likeArticle(articleId: string): void {
    this.likeService.likeArticle(articleId)
      .then(response => {
        const status = response?.status;
        if (status === 'like') {
          this.addLike(articleId);
        } else if (status === 'dislike') {
          this.deleteLike(articleId);
        } else {
          console.error(`Unknown response: ${status}`);
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  showLikes(articleId: string): void {
    this.likeService.showLikes(articleId)
      .then(response => {
        const users = response.users;
        if (users.length > 0) {
          const userNames = users.map((user: string) => `<div>${user}</div>`).join('');
          const tooltipContent = `<div class="article-card__like-tooltip-content">${userNames}</div>`;

          const tooltip = document.createElement('div');
          tooltip.classList.add('article-card__like-tooltip');
          tooltip.innerHTML = tooltipContent;

          const likeButton = document.querySelector(`#like-container-${articleId}`);
          likeButton?.appendChild(tooltip);
        }
      })
      .catch(error => {
        console.error(error);
      });
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
