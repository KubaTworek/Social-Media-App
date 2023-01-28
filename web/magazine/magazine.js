export class Magazine extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    set magazine(magazine) {
        this.shadowRoot.innerHTML = `
            <style>
                .magazine-card {
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

            <div class="magazine-card">
                <p>Magazine: ${magazine.name}</p>
                <ul class="articles"></ul>
                <p id="idPara">${magazine.id}</p>
                <button id="delete-button">Delete</button>
            </div>
        `
        const deleteBtn = this.shadowRoot.querySelector('#delete-button');
        const articlesEl = this.shadowRoot.querySelector('.articles');
        deleteBtn.addEventListener('click', this.delete.bind(this, magazine.id));

        const articles = magazine.articles
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

        fetch('http://localhost:8887/magazines/' + idParaEl.innerHTML, {
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

customElements.define('magazine-post', Magazine)