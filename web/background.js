import {ArticlePost} from "./article/article-post.js";
import {ArticleForm} from './article/article-form.js';
import {AuthorPost} from './author/author-post.js';
import {AuthorForm} from './author/author-form.js';
import {MagazinePost} from './magazine/magazine-post.js';
import {MagazineForm} from './magazine/magazine-form.js';

class Background extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
        this.setAttribute('opened', '')
    }

    connectedCallback() {
        this.shadowRoot.innerHTML = this.render();

        this.data = 'articles';
        this.background = this.shadowRoot.getElementById('background')
        this.dataList = this.shadowRoot.getElementById('data-list')
        this.input = this.shadowRoot.getElementById('search-input')
        this.getData()

        const articleBtn = this.shadowRoot.getElementById('article-button')
        const authorBtn = this.shadowRoot.getElementById('author-button')
        const magazineBtn = this.shadowRoot.getElementById('magazine-button')
        const searchBtn = this.shadowRoot.getElementById('search-button')
        const addBtn = this.shadowRoot.getElementById('add-button')

        articleBtn.addEventListener('click', () => {
            this.data = 'articles'
            this.getData()
        })
        authorBtn.addEventListener('click', () => {
            this.data = 'authors'
            this.getData()
        })
        magazineBtn.addEventListener('click', () => {
            this.data = 'magazines'
            this.getData()
        })
        searchBtn.addEventListener('click', this.getData.bind(this))
        addBtn.addEventListener('click', this.postData.bind(this))
    }

    getData() {
        this.dataList.innerHTML = ""
        switch (this.data) {
            case 'articles':
                this.getArticles()
                    .catch(err => console.log(err))
                break;
            case 'authors':
                this.getAuthors()
                    .catch(err => console.log(err))
                break;
            case 'magazines':
                this.getMagazines()
                    .catch(err => console.log(err))
                break;
            default:
                console.log('Wrong data')
        }
    }

    postData() {
        switch (this.data) {
            case 'articles':
                const articleForm = new ArticleForm()
                this.background.appendChild(articleForm)
                articleForm.open()
                break;
            case 'authors':
                const authorForm = new AuthorForm()
                this.background.appendChild(authorForm)
                authorForm.open()
                break;
            case 'magazines':
                const magazineForm = new MagazineForm()
                this.background.appendChild(magazineForm)
                magazineForm.open()
                break;
            default:
                console.log('Wrong data')
        }
    }

    async getArticles() {
        const url = "http://localhost:8887/articles/" + this.input.value

        const response = await fetch(url)
            .then(r => r.json())
            .catch(err => console.log(err))

        response.forEach(article => {
                const li = document.createElement('li')
                const el = new ArticlePost()
                el.article = article
                li.appendChild(el)
                this.dataList.appendChild(li)
            }
        )
    }

    async getAuthors() {
        const url = "http://localhost:8887/authors/" + this.input.value

        const response = await fetch(url)
            .then(r => r.json())
            .catch(err => console.log(err))

        response.forEach(author => {
                const li = document.createElement('li')
                const el = new AuthorPost()
                el.author = author
                li.appendChild(el)
                this.dataList.appendChild(li)
            }
        )
    }

    async getMagazines() {
        const url = "http://localhost:8887/magazines/" + this.input.value

        const response = await fetch(url)
            .then(r => r.json())
            .catch(err => console.log(err))

        response.forEach(magazine => {
                const li = document.createElement('li')
                const el = new MagazinePost()
                el.magazine = magazine
                li.appendChild(el)
                this.dataList.appendChild(li)
            }
        )
    }

    render() {
        return `
            <style>
                #background {
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
                
                .data-container {
                    width: 100%;
                }
                
                #data-list {
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
            
            <div id="background">
                <div class="buttons-container">
                    <button id="article-button">Articles</button><button id="author-button">Authors</button><button id="magazine-button">Magazines</button>
                </div>
                    <div class="search-bar"><input id="search-input" type="text"><button id="search-button">Search</button></div>
                    <div class="button-container"><button id="add-button">Add</button></div>
                    <div class="data-container">         
                        <ul id="data-list">

                        </ul>
                    </div>
            </div>
        `;
    }
}

customElements.define('backdrop-comp', Background)