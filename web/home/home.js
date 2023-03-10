import {ArticleCard} from "./article-card.js";
import {Http} from '../http/http.js';
import {config} from "../config.js";
import {ArticlePost} from "./article-post.js";
import {Authorization} from "../auhtorization/authorization-modal.js";

export class Home extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
        this.setAttribute('opened', '')
    }

    connectedCallback() {
        this.shadowRoot.innerHTML = this.render();

        this.dataList = this.shadowRoot.getElementById('data-list');
        this.input = this.shadowRoot.getElementById('search-input');
        this.getData()

        this.shadowRoot.getElementById('search-input')
            .addEventListener('keyup', (event) => {
                this.getData(event);
            });

        this.authorizationBoard = this.shadowRoot.getElementById('authorization-board');
        const authorization = new Authorization()
        this.authorizationBoard.appendChild(authorization)

        this.inputBar = this.shadowRoot.getElementById('input-bar');
        const articlePost = new ArticlePost()
        this.inputBar.appendChild(articlePost)
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

    async getArticles() {
        Http.getInstance()
            .doGet(config.articlesUrl + this.input.value)
            .then(articles => this.renderArticles(articles))
            .catch(err => console.log(err));
    }

    renderArticles(authors) {
        authors.forEach(article => {
            const li = document.createElement('li');
            const el = new ArticleCard();
            el.article = article;
            li.appendChild(el);
            this.dataList.appendChild(li);
        });
    }

    render() {
        return `
            <style>
              #background {
                max-width: 40rem;
                align-items: center;
                display: flex;
                flex-direction: column;
                justify-content: center;
                margin: auto;
              }
              
              #authorization-board {
                display: flex;
                align-items: center;
                flex-direction: column;
                justify-content: center;
              }
              
              #main-board {
                align-items: center;
                display: flex;
                flex-direction: column;
                height: 100%;
                margin: auto;
                width: 80%;
              }
              
              #search-input {
                background-color: #444;
                border: 1px solid #444;
                border-radius: 8px;
                box-sizing: border-box;
                color: #eee;
                font-size: 1rem;
                height: 2rem;
                margin: 0.4rem auto;
                outline: none;
                padding: 0.5rem;
                width: 80%;
              }
              
              #input-bar {
                align-items: center;
                border: 1px solid #444;
                box-sizing: border-box;
                display: flex;
                flex-direction: column;
                justify-content: right;
                position: relative;
                width: 100%;
                height: 10rem;
              }
              
              #data-list {
                border-left: 1px solid #444;
                border-right: 1px solid #444;
                box-sizing: border-box;
                list-style-type: none;
                margin: 0;
                padding: 0;
                width: 100%;
              }
              
              article-post {
                width: 100%;
                height: 100%;
              }
              
              @media screen and (min-width: 860px) {
                #background {
                  flex-direction: row;
                  align-items: flex-start;
                  max-width: 100%;
                  margin: auto;
                }
                
                #authorization-board {
                  order:1;
                  width: 15rem;
                  align-items: flex-end;
                }
                
                authorization-modal {
                  display: flex;
                  align-items: flex-end;
                  flex-direction: column;
                }
                
                #search-input {
                  order:3;
                  width: 15rem;
                  margin: 0 0 0 1rem;
                }
                
                #main-board {
                  order:2;
                  max-width: 35rem;
                  margin:0;
                }
              }
            </style>
            <div id="background"> 
              <div id="authorization-board"></div>
              <input id="search-input" type="text" placeholder="Search"> 
              <div id="main-board">
                <div id="input-bar"></div>
                <ul id="data-list"></ul>
              </div>
            </div>
        `;
    }
}

customElements.define('home-modal', Home)