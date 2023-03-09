import { AuthorPost } from './author-post.js';
import { AuthorForm } from './author-form.js';
import { Http } from '../http/http.js';
import { RouterHandler } from '../router/router-handler.js';
import { config } from "../config.js";


export class AuthorModal extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
        this.setAttribute('opened', '');
    }

    connectedCallback() {
        this.shadowRoot.innerHTML = this.render();

        this.background = this.shadowRoot.getElementById('background');
        this.dataList = this.shadowRoot.getElementById('data-list');
        this.input = this.shadowRoot.getElementById('search-input');
        this.getData();

        this.shadowRoot.getElementById('article-button')
            .addEventListener('click',
                () => RouterHandler.getInstance().router.navigate('/articles'))

        this.shadowRoot.getElementById('author-button')
            .addEventListener('click',
                () => RouterHandler.getInstance().router.navigate('/authors'))

        this.shadowRoot.getElementById('search-button')
            .addEventListener('click', this.getData.bind(this))
        this.shadowRoot.getElementById('add-button')
            .addEventListener('click', this.postData.bind(this))
    }

    getData() {
        this.dataList.innerHTML = '';
        this.getAuthors().catch(err => console.log(err));
    }

    postData() {
        const authorForm = new AuthorForm();
        this.background.appendChild(authorForm);
        authorForm.open();
    }

    async getAuthors() {
        Http.getInstance()
            .doGet(config.authorsUrl + this.input.value)
            .then(authors => this.renderAuthors(authors))
            .catch(err => console.log(err));
    }

    renderAuthors(authors) {
        authors.forEach(author => {
            const li = document.createElement('li');
            const el = new AuthorPost();
            el.author = author;
            li.appendChild(el);
            this.dataList.appendChild(li);
        });
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
                    <button id="article-button">Articles</button><button id="author-button">Authors</button>
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