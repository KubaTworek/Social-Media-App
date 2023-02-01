import {DeletePopup} from "../utils/delete-popup.js";

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

        this.magazineCard = this.shadowRoot.querySelector('.magazine-card');
        this.idMagazine = this.shadowRoot.getElementById('idMagazine');
        this.articlesList = this.shadowRoot.querySelector('.articles');
        this.renderArticles(magazine.articles)

        this.shadowRoot.getElementById('delete-button')
            .addEventListener('click', this.delete.bind(this));
    }

    delete() {
        const deletePopup = new DeletePopup(
            "Would you like to delete magazine",
            'magazines/' + this.idMagazine.innerHTML)
        this.magazineCard.appendChild(deletePopup)
        deletePopup.open()
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