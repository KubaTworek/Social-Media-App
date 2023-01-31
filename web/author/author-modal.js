
import {AuthorPost} from './author-post.js';
import {AuthorForm} from './author-form.js';
import {Http} from '../http/http.js';
import {RouterHandler} from "../router/router-handler.js";

export class AuthorModal extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
        this.setAttribute('opened', '')
    }

    connectedCallback() {
        this.shadowRoot.innerHTML = this.render();

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
            RouterHandler.getInstance.router.navigate('/articles');
        })
        authorBtn.addEventListener('click', () => {
            RouterHandler.getInstance.router.navigate('/authors');
        })
        magazineBtn.addEventListener('click', () => {
            RouterHandler.getInstance.router.navigate('/magazines');
        })
        searchBtn.addEventListener('click', this.getData.bind(this))
        addBtn.addEventListener('click', this.postData.bind(this))
    }

    getData() {
        this.dataList.innerHTML = ""
        this.getAuthors()
            .catch(err => console.log(err))
    }

    postData() {
        const authorForm = new AuthorForm()
        this.background.appendChild(authorForm)
        authorForm.open()
    }

    async getAuthors() {
        Http.instance.doGet("authors/" + this.input.value).then(r => {
            r.forEach(author => {
                    const li = document.createElement('li')
                    const el = new AuthorPost()
                    el.author = author
                    li.appendChild(el)
                    this.dataList.appendChild(li)
                }
            )
        })
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
                
                li {
                    width: 100%;
                }
                
                author-post{
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

customElements.define('author-modal', AuthorModal)