export class Author extends HTMLElement {
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
                
                #idPara {
                    display: none;
                }
                
            </style>

            <div class="author-card">
                <p>Author: ${author.fistName} ${author.lastName}</p>
                <ul class="articles"></ul>
                <p id="idPara">${author.id}</p>
                <button id="delete-button">Delete</button>
            </div>
        `
        const deleteBtn = this.shadowRoot.querySelector('#delete-button');
        const articlesEl = this.shadowRoot.querySelector('.articles');
        deleteBtn.addEventListener('click', this.delete.bind(this, author.id));

        const articles = author.articles
        articles.forEach(article => {
                const li = document.createElement('li')
                const el = document.createElement('p')
                el.innerText = article.title
                li.appendChild(el)
                articlesEl.appendChild(li)
            }
        )

    }

    delete() {
        const idParaEl = this.shadowRoot.querySelector('#idPara');

        fetch('http://localhost:8887/authors/' + idParaEl.innerHTML, {
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

customElements.define('author-post', Author)