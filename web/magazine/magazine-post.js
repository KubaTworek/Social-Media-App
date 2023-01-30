export class MagazinePost extends HTMLElement {
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
                
                #idMagazine {
                    display: none;
                }                
            </style>

            <div class="magazine-card">
                <p>Magazine: ${magazine.name}</p>
                <ul class="articles"></ul>
                <p id="idMagazine">${magazine.id}</p>
                <button id="delete-button">Delete</button>
            </div>
        `

        this.idMagazine = this.shadowRoot.getElementById('idMagazine');
        this.articlesList = this.shadowRoot.querySelector('.articles');
        this.renderArticles(magazine.articles)

        const deleteBtn = this.shadowRoot.getElementById('delete-button');
        deleteBtn.addEventListener('click', this.delete.bind(this));
    }

    delete() {
        fetch('http://localhost:8887/magazines/' + this.idMagazine.innerHTML, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json'
            }
        }).catch(err => console.log(err))
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

customElements.define('magazine-post', MagazinePost)