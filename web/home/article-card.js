import {DeleteForm} from "../authorization/delete-form.js";
import {config} from "../config.js";

export class ArticleCard extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    set setArticle(article) {
        this.article = article;
        this.render();
    }

    delete() {
        const deletePopup = new DeleteForm(
            "Would you like to delete Article",
            config.articlesUrl + this.article.id);
        this.articleCard.appendChild(deletePopup);
        deletePopup.open();
    }

    render() {
        const { author_firstName, author_lastName, author_username, text, id } = this.article || {};

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
                <p class="name">${author_firstName} ${author_lastName} <span class="username">@${author_username}</span></p>
                <p class="content">${text}</p>
                <p class="id">${id}</p>
                <button id="delete-button">Delete</button>
            </div>
        `;

        this.articleCard = this.shadowRoot.querySelector('.article-card');
        this.id = this.shadowRoot.querySelector('.id');
        this.shadowRoot.getElementById('delete-button').addEventListener('click', this.delete.bind(this));
    }
}

customElements.define('article-card', ArticleCard)