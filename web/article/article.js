export class Article extends HTMLElement {
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
                
                #idPara {
                    display: none;
                }
                
            </style>

            <div class="article-card">
                <h3>${article.title}</h3>
                <p>${article.text}</p>
                <p>Magazine: ${article.magazine}</p>
                <p>Author: ${article.author_firstName} ${article.author_lastName}</p>
                <p id="idPara">${article.id}</p>
                <button id="delete-button">Delete</button>
            </div>
        `
        const deleteBtn = this.shadowRoot.querySelector('#delete-button');
        deleteBtn.addEventListener('click', this.delete.bind(this, article.id));
    }

    delete() {
        const idParaEl = this.shadowRoot.querySelector('#idPara');

        fetch('http://localhost:8887/articles/' + idParaEl.innerHTML, {
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

customElements.define('article-post', Article)