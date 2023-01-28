import './article/article.js';
import './article/articleForm.js';
import './author/author.js';
import './author/authorForm.js';
import './magazine/magazine.js';

class Backdrop extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
        this.setAttribute('opened', '')
        this.articles = false;
        this.authors = false;
        this.magazines = false;

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
                
                .authors {
                    width: 100%;
                    list-style-type: none;
                    padding: 0;
                }
                
                .magazines {
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
                
                author-post{
                    display: flex;
                    justify-content: center;
                }
                
                magazine-post{
                    display: flex;
                    justify-content: center;
                }
            </style>
            
            <div class="backdrop">
                <div class="buttons-container">
                    <button id="article-button">Articles</button><button id="author-button">Authors</button><button id="magazine-button">Magazines</button>
                </div>
                    <article-form></article-form>
                    <author-form></author-form>
                    <div class="search-bar"><input id="search-input" type="text"><button id="search-button">Search</button></div>
                    <div class="button-container"><button id="add-button">Add</button></div>
                    <div class="article-container">
                        
                        <ul class="articles">

                        </ul>
                        <ul class="authors">

                        </ul>
                        <ul class="magazines">

                        </ul>
                    </div>
            </div>
        `;
        const articleBtn = this.shadowRoot.getElementById('article-button')
        const authorBtn = this.shadowRoot.getElementById('author-button')
        const magazineBtn = this.shadowRoot.getElementById('magazine-button')
        const searchBtn = this.shadowRoot.getElementById('search-button')
        const addBtn = this.shadowRoot.getElementById('add-button')

        articleBtn.addEventListener('click', this.articleOn.bind(this))
        authorBtn.addEventListener('click', this.authorOn.bind(this))
        magazineBtn.addEventListener('click', this.magazineOn.bind(this))
        searchBtn.addEventListener('click', this.getData.bind(this))
        addBtn.addEventListener('click', this.postData.bind(this))
    }


    articleOn(){
        const articlesEl = this.shadowRoot.querySelector('.articles')
        const authorsEl = this.shadowRoot.querySelector('.authors')
        const magazinesEl = this.shadowRoot.querySelector('.magazines')

        this.articles = true;
        this.authors = false;
        this.magazines = false;

        authorsEl.innerHTML = ""
        articlesEl.innerHTML = ""
        magazinesEl.innerHTML = ""

        this.getData()
    }

    authorOn(){
        const articlesEl = this.shadowRoot.querySelector('.articles')
        const authorsEl = this.shadowRoot.querySelector('.authors')
        const magazinesEl = this.shadowRoot.querySelector('.magazines')

        this.articles = false;
        this.authors = true;
        this.magazines = false;

        articlesEl.innerHTML = ""
        authorsEl.innerHTML = ""
        magazinesEl.innerHTML = ""

        this.getData()
    }

    magazineOn(){
        const articlesEl = this.shadowRoot.querySelector('.articles')
        const authorsEl = this.shadowRoot.querySelector('.authors')
        const magazinesEl = this.shadowRoot.querySelector('.magazines')

        this.authors = false;
        this.articles = false;
        this.magazines = true;

        articlesEl.innerHTML = ""
        authorsEl.innerHTML = ""
        magazinesEl.innerHTML = ""

        this.getData()
    }

    getData(){
        if(this.articles === true) {
            this.getArticles()
        }
        if(this.authors === true){
            this.getAuthors()
        }
        if(this.magazines === true){
            this.getMagazines()
        }
    }

    postData(){
        const articleForm = this.shadowRoot.querySelector('article-form')
        const authorForm = this.shadowRoot.querySelector('author-form')
        if (!articleForm.isOpen && !authorForm.isOpen) {
            if(this.articles === true) {
                articleForm.open()
            }
            if(this.authors === true){
                authorForm.open()
            }
        }

    }

    async getArticles() {
        const articlesEl = this.shadowRoot.querySelector('.articles')
        const authorsEl = this.shadowRoot.querySelector('.authors')
        const magazinesEl = this.shadowRoot.querySelector('.magazines')
        const input = this.shadowRoot.getElementById('search-input')

        const url = "http://localhost:8887/articles/" + input.value

        const response = await fetch(url)
        const json = await response.json()

        articlesEl.innerHTML = ""
        authorsEl.innerHTML = ""
        magazinesEl.innerHTML = ""

        json.forEach(article => {
                const li = document.createElement('li')
                const el = document.createElement('article-post')
                el.article = article
                li.appendChild(el)
                articlesEl.appendChild(li)
            }
        )
    }

    async getAuthors() {
        const articlesEl = this.shadowRoot.querySelector('.articles')
        const authorsEl = this.shadowRoot.querySelector('.authors')
        const magazinesEl = this.shadowRoot.querySelector('.magazines')
        const input = this.shadowRoot.getElementById('search-input')

        const url = "http://localhost:8887/authors/" + input.value

        const response = await fetch(url)
        const json = await response.json()

        authorsEl.innerHTML = ""
        articlesEl.innerHTML = ""
        magazinesEl.innerHTML = ""

        json.forEach(author => {
                const li = document.createElement('li')
                const el = document.createElement('author-post')
                el.author = author
                li.appendChild(el)
                authorsEl.appendChild(li)
            }
        )
    }

    async getMagazines() {
        const articlesEl = this.shadowRoot.querySelector('.articles')
        const authorsEl = this.shadowRoot.querySelector('.authors')
        const magazinesEl = this.shadowRoot.querySelector('.magazines')
        const input = this.shadowRoot.getElementById('search-input')

        const url = "http://localhost:8887/magazines/" + input.value

        const response = await fetch(url)
        const json = await response.json()

        authorsEl.innerHTML = ""
        articlesEl.innerHTML = ""
        magazinesEl.innerHTML = ""

        json.forEach(magazine => {
                const li = document.createElement('li')
                const el = document.createElement('magazine-post')
                el.magazine = magazine
                li.appendChild(el)
                magazinesEl.appendChild(li)
            }
        )
    }
}

customElements.define('backdrop-comp', Backdrop)