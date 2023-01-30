import {Http} from "../http/http.js";

export class AuthorPost extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    set author(author) {
        this.shadowRoot.innerHTML = `
            <style>
                .author-card {
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
                
                #idAuthor {
                    display: none;
                }
            </style>

            <div class="author-card">
                <p>Author: ${author.firstName} ${author.lastName}</p>
                <ul class="articles"></ul>
                <p id="idAuthor">${author.id}</p>
                <button id="delete-button">Delete</button>
            </div>
        `

        this.idAuthor = this.shadowRoot.getElementById('idAuthor');
        this.articlesList = this.shadowRoot.querySelector('.articles');
        this.renderArticles(author.articles)

        const deleteBtn = this.shadowRoot.getElementById('delete-button');
        deleteBtn.addEventListener('click', this.delete.bind(this));
    }

    delete() {
        Http.instance.doDelete("authors/" + this.idAuthor.innerHTML)
            .catch(err => console.log(err))
        this.remove()
    }

    renderArticles(articles) {
        articles.forEach(article => {
                const li = document.createElement('li')
                const el = document.createElement('p')
                el.innerText = article.title
                li.appendChild(el)
                this.articlesList.appendChild(li)
            }
        )
    }
}

customElements.define('author-post', AuthorPost)