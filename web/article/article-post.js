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

        this.idArticle = this.shadowRoot.getElementById('idArticle');
        const deleteBtn = this.shadowRoot.getElementById('delete-button');
        deleteBtn.addEventListener('click', this.delete.bind(this));
    }

    delete() {
        fetch('http://localhost:8887/articles/' + this.idArticle.innerHTML, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(r => r.json())
            .then(data => console.log(data))
            .catch(err => console.log(err))
        this.remove()
    }
}

customElements.define('article-post', ArticlePost)