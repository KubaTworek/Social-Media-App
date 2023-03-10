import {DeletePopup} from "../utils/delete-popup.js";
import {config} from "../config.js";


export class ArticleCard extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    set article(article) {
        this.shadowRoot.innerHTML = `
            <style>
              .article-card {
                background-color: #111;
                border-bottom: 1px solid #444;
                border-top: none;
                box-sizing: border-box;
                color: #eee;
                display: block;
                padding: 0 0.8rem;
              }
              
              .name {
                font-weight: 700;
                text-align: left;
              }
              
              .username {
                color: #666;
                font-weight: 500;
                text-align: left;
              }
              
              .content {
                text-align: justify;
              }
              
              .id {
                display: none;
              }             
            </style>

            <div class="article-card">
                <p class="name">${article.author_firstName} ${article.author_lastName} <span class="username">@${article.author_username}</span></p>
                <p class="content">${article.text}</p>
                <p class="id">${article.id}</p>
                <button id="delete-button">Delete</button>
            </div>
        `
        this.articleCard = this.shadowRoot.querySelector('.article-card')
        this.idArticle = this.shadowRoot.querySelector('.id');
        this.shadowRoot.getElementById('delete-button')
            .addEventListener('click', this.delete.bind(this));
    }

    delete() {
        const deletePopup = new DeletePopup(
            "Would you like to delete Article",
            config.articlesUrl + this.idArticle.innerHTML)
        this.articleCard.appendChild(deletePopup)
        deletePopup.open()
    }
}

customElements.define('article-card', ArticleCard)