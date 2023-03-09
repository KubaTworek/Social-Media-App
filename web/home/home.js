import {ArticlePost} from "../article/article-post.js";
import {ArticleForm} from '../article/article-form.js';
import {Http} from '../http/http.js';
import {config} from "../config.js";

export class Home extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
        this.setAttribute('opened', '')
    }

    connectedCallback() {
        this.shadowRoot.innerHTML = this.render();

        this.background = this.shadowRoot.getElementById('background');
        this.dataList = this.shadowRoot.getElementById('data-list');
        this.input = this.shadowRoot.getElementById('search-input');
        this.getData()

        this.shadowRoot.getElementById('search-input')
            .addEventListener('keyup', (event) => {
                this.getData(event);
            });
        this.shadowRoot.getElementById('add-button')
            .addEventListener('click', this.postData.bind(this))
    }

    getData(event) {
        const clearDataListAndFetch = () => {
            this.dataList.innerHTML = "";
            this.getArticles().catch(err => console.log(err));
        };

        if (event && event.key === "Enter") {
            event.preventDefault();
            clearDataListAndFetch();
            this.input.value = "";
        } else if (this.input.value === "") {
            clearDataListAndFetch();
        }
    }

    postData() {
        const articleForm = new ArticleForm();
        this.background.appendChild(articleForm);
        articleForm.open();
    }

    async getArticles() {
        Http.getInstance()
            .doGet(config.articlesUrl + this.input.value)
            .then(articles => this.renderArticles(articles))
            .catch(err => console.log(err));
    }

    renderArticles(authors) {
        authors.forEach(article => {
            const li = document.createElement('li');
            const el = new ArticlePost();
            el.article = article;
            li.appendChild(el);
            this.dataList.appendChild(li);
        });
    }

    render() {
        return `
            <style>
                #main-board {
                    max-width: 30rem;
                    height: 100%;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    flex-direction: column;
                    margin: auto;
                }
                
                #search-input {
                    width: 100%;
                    height: 2rem;
                    background-color: #444;
                    font-size:1rem;
                    color: #eee;
                    border: 1px solid #444;
                    border-radius: 8px;
                    
                    outline: none;
                    padding: 0.5rem;
                    margin: 0.4rem 0;
                    box-sizing: border-box;
                }
                
                #post-input {
                    width: 100%;
                    position: relative;
                    justify-content: right;
                    align-items: center;
                    flex-direction: column;
                    border: 1px solid #444;
                    box-sizing: border-box;
                    border-bottom: none;
                }
                
                #post-content {
                    width: 100%;
                    height: 5rem;
                    background-color: #111;
                    color: #eee;
                    font-size: 1.2rem;
                    padding: 0.5rem;
                    
                    box-sizing: border-box;
                    resize: none;
                    outline: none;
                    border: none;
                }
                
                #add-button {
                    position: absolute;
                    right: 2%;
                    top: 50%;
                    
                    background-color: #ff0000;;
                    border: none;
                    border-radius: 9999px;
                    color: #eee;
                    cursor: pointer;
                    font-size: 0.9rem;
                    font-weight: 700;
                    padding: 6px 14px;
                    text-align: center;
                    text-decoration: none;
                    transition: background-color 0.3s ease-out;
                }
                
                #add-button:hover {
                    background-color: #cc0000;
                }
              
                #data-list {
                    width: 100%;
                    list-style-type: none;
                    padding: 0;
                    margin: 0;
                    box-sizing: border-box
                }
            </style>
            
            <div id="main-board">
                    <input id="search-input" type="text" placeholder="Search">
                    <div id="post-input">
                        <textarea id="post-content" placeholder="What's happening?"></textarea>
                        <button id="add-button">SEND</button>
                    </div>
                    <ul id="data-list">

                    </ul>
            </div>
        `;
    }
}

customElements.define('home-modal', Home)