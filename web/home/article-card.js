import {DeleteForm} from "../form/delete-form.js";
import {config} from "../config/config.js";
import {Http} from "../config/http.js";

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

    like() {
        const headers = {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Authorization": sessionStorage.getItem("jwt")
        };

        Http.getInstance()
            .doPost(`${config.articlesUrl}like/${this.id}`, null, headers)
            .then(response => {
                const status = response.status;
                if (status === 'like') {
                    this.addLike();
                } else if (status === 'dislike') {
                    this.deleteLike();
                } else {
                    console.error(`Unknown response: ${status}`);
                }
            })
            .catch((error) => {
                console.error(`Failed to add like: ${error}`);
            });
    }

    addLike() {
        const likesElement = this.shadowRoot.querySelector('.likes');
        const currentLikes = parseInt(likesElement.innerText);
        likesElement.innerText = currentLikes + 1;
    }

    deleteLike() {
        const likesElement = this.shadowRoot.querySelector('.likes');
        const currentLikes = parseInt(likesElement.innerText);
        likesElement.innerText = currentLikes - 1;
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
        const {author_firstName, author_lastName, author_username, text, id, timestamp, numOfLikes} = this.article || {};
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
              
              #like-button {
                font-size: 1.2rem;
                cursor: pointer;
              }
            
              .likes {
                font-size: 1rem;
                margin-left: 5px;
              }             
            </style>
    
            <div class="article-card">
                <p class="name">${author_firstName} ${author_lastName} <span class="username">@${author_username}  \u2022  ${newTimestamp}</span></p>
                <p class="content">${text}</p>
                <p class="id">${id}</p>
                <div>
                    <p><span id="like-button">👍</span><span class="likes">${numOfLikes}</span>   </p>             
                    <button id="delete-button">Delete</button>
                </div>
            </div>
        `;

        this.articleCard = this.shadowRoot.querySelector('.article-card');
        this.id = this.shadowRoot.querySelector('.id').innerText;
        this.shadowRoot.getElementById('delete-button').addEventListener('click', this.delete.bind(this));
        this.shadowRoot.getElementById('like-button').addEventListener('click', this.like.bind(this));
    }
}

customElements.define('article-card', ArticleCard)