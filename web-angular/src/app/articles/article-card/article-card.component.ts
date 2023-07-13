import {Component, Input, ViewChild, ViewEncapsulation} from "@angular/core";
import {Article} from "../dto/article.type";
import {LikeService} from "../like.service";
import {ArticleDeleteComponent} from "../article-delete/article-delete.component";
import {ArticleService} from "../article.service";

@Component({
  selector: 'article-card',
  templateUrl: './article-card.component.html',
  styleUrls: ['./article-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArticleCardComponent {
  @Input() article: Article = {
    author_firstName: '',
    author_lastName: '',
    author_username: '',
    timestamp: new Date(),
    text: '',
    id: '',
    numOfLikes: '',
    elapsed: ''
  };

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

          const likeButton = document.querySelector(`#like-button-${articleId}`);
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
    const likeButton = document.querySelector(`#like-button-${articleId}`);
    const tooltip = likeButton?.querySelector('.like-tooltip');
    if (tooltip) {
      likeButton?.removeChild(tooltip);
    }
  }

  private addLike(articleId: string): void {
    const likesElement = document.querySelector(`#likes-${articleId}`) as HTMLElement;
    const currentLikes = parseInt(likesElement.innerText);
    likesElement.innerText = (currentLikes + 1).toString();
  }

  private deleteLike(articleId: string): void {
    const likesElement = document.querySelector(`#likes-${articleId}`) as HTMLElement;
    const currentLikes = parseInt(likesElement.innerText);
    likesElement.innerText = (currentLikes - 1).toString();
  }
}
