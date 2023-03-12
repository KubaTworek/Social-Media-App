import {DeleteForm} from "../form/delete-form.js";
import {config} from "../config/config.js";

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
            "Would you like to delete Article?",
            config.articlesUrl + this.article.id);
        this.articleCard.appendChild(deletePopup);
        deletePopup.open();
    }

    getTimeElapsed(timestamp) {
        const now = new Date();
        const timeDiff = now.getTime() - timestamp.getTime();

        if (timeDiff < 60000) { // mniej niż 1 minuta
            const seconds = Math.floor(timeDiff / 1000);
            return seconds + "s";
        } else if (timeDiff < 3600000) { // mniej niż 1 godzina
            const minutes = Math.floor(timeDiff / 60000);
            return minutes + "m";
        } else if (timeDiff < 86400000) { // mniej niż 1 dzień
            const hours = Math.floor(timeDiff / 3600000);
            return hours + "h";
        } else if (timeDiff < 604800000) { // mniej niż 1 tydzień
            const days = Math.floor(timeDiff / 86400000);
            return days + "day";
        } else {
            const weeks = Math.floor(timeDiff / 604800000);
            return weeks + "week";
        }
    }

    render() {
        const {author_firstName, author_lastName, author_username, text, id, timestamp} = this.article || {};
        const timestampJs = new Date(timestamp);
        const newTimestamp = this.getTimeElapsed(timestampJs)

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
                <p class="name">${author_firstName} ${author_lastName} <span class="username">@${author_username}  \u2022  ${newTimestamp}</span></p>
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