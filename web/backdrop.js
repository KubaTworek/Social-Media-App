import './article/article.js';
import './article/articleForm.js';

class Backdrop extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
        this.setAttribute('opened', '')

        this.shadowRoot.innerHTML = `
            <style>
                .backdrop {
                    width: 50%;
                    height: 100%;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    flex-direction: column;
                    margin: auto;
                }
                
                .search-bar {
                    width: 100%;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    text-align: center;
                    margin: 1rem auto;
                }
                
                #search-input {
                    width: 70%;
                }
                
                #search-button {
                    width: 30%;
                }
                
                .button-container {
                    width: 100%;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                }
                
                #add-button {
                    width: 30%;
                    margin 2rem;
                }
                
                .article-container {
                    width: 100%;
                }
                
                .articles {
                    width: 100%;
                    list-style-type: none;
                    padding: 0;
                }
                
                li {
                    width: 100%;
                }
                
                article-post{
                    display: flex;
                    justify-content: center;
                }
            </style>
            
            <div class="backdrop">
                    <article-form></article-form>
                    <div class="search-bar"><input id="search-input" type="text"><button id="search-button">Search</button></div>
                    <div class="button-container"><button id="add-button">Add new Article</button></div>
                    <div class="article-container">
                        
                        <ul class="articles">

                        </ul>
                    </div>
            </div>
        `;
        this.getArticles()
        const searchBtn = this.shadowRoot.getElementById('search-button')
        const addBtn = this.shadowRoot.getElementById('add-button')
        const articleForm = this.shadowRoot.querySelector('article-form')

        searchBtn.addEventListener('click', this.getArticles.bind(this))
        addBtn.addEventListener('click', () => {
            if (!articleForm.isOpen) {
                articleForm.open()
                console.log(articleForm)
            }
        })
    }

    async getArticles() {
        const articlesEl = this.shadowRoot.querySelector('.articles')
        const input = this.shadowRoot.getElementById('search-input')

        const url = "http://localhost:8887/articles/" + input.value

        const response = await fetch(url)
        const json = await response.json()

        articlesEl.innerHTML = ""
        json.forEach(article => {
                const li = document.createElement('li')
                const el = document.createElement('article-post')
                el.article = article
                li.appendChild(el)
                articlesEl.appendChild(li)
            }
        )
    }
}

customElements.define('backdrop-comp', Backdrop)