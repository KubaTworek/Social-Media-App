import {DeletePopup} from "../utils/delete-popup.js";

export class ArticlePost extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    set article(article) {
        this.shadowRoot.innerHTML = `
            <style>
                .article-card {
                    width: 60%;
                    display: block;
                    background-color: blanchedalmond;
                    border-radius: 5px;
                    padding: 1rem;
                    margin: 1rem;
                }
                
                h3 {
                    text-align: center;
                    font-size: 1.5rem;
                }
                
                #idArticle {
                    display: none;
                }             
            </style>

            <div class="article-card">
                <h3>${article.title}</h3>
                <p>${article.text}</p>
                <p>Magazine: ${article.magazine}</p>
                <p>Author: ${article.author_firstName} ${article.author_lastName}</p>
                <p id="idArticle">${article.id}</p>
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
            'articles/' + this.idArticle.innerHTML)
        this.articleCard.appendChild(deletePopup)
        deletePopup.open()
    }
}

customElements.define('article-post', ArticlePost)