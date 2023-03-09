import {DeletePopup} from "../utils/delete-popup.js";
import { config } from "../config.js";


export class ArticlePost extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    set article(article) {
        this.shadowRoot.innerHTML = `
            <style>
                .article-card {
                    display: block;
                    background-color: #111;
                    color: #eee;
                    border: 1px solid #444;
                    padding: 0 0.8rem;
                    box-sizing: border-box;

                }
                
                .name {
                    text-align: left;
                    font-weight: 700;
                }
                
                .username {
                    text-align: left;
                    color: #666;
                    font-weight: 500;
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
        this.idArticle = this.shadowRoot.getElementById('idArticle');
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

customElements.define('article-post', ArticlePost)