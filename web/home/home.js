import {ArticleCard} from "./article-card.js";
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

        this.dataList = this.shadowRoot.getElementById('data-list');
        this.input = this.shadowRoot.getElementById('search-input');
        this.getData()

        this.shadowRoot.getElementById('search-input')
            .addEventListener('keyup', (event) => {
                this.getData(event);
            });
        this.shadowRoot.getElementById('send-button')
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

    postData = (event) => {
        event.preventDefault();
        const content = this.shadowRoot.getElementById("post-content");
        const data = {
            title: "title",
            text: content.value
        }
        sessionStorage.setItem("jwt", "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5WCIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY3ODQ0MjQzMywiZXhwIjoxNjc4NDUzMjAzfQ.5wrFWn7_nG5XOfAzgf-Qh1V1OQD2HJKf5utI2hCNzlU")
        const headers = {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Authorization": sessionStorage.getItem("jwt")
        };
        Http.getInstance().doPost(config.articlesUrl, JSON.stringify(data), headers)
            .then(() => location.reload())
            .catch((err) => console.error(err));
    };

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
              #main-board {
                align-items: center;
                display: flex;
                flex-direction: column;
                height: 100%;
                justify-content: center;
                margin: auto;
                max-width: 30rem;
              }
              
              #search-input {
                background-color: #444;
                border: 1px solid #444;
                border-radius: 8px;
                box-sizing: border-box;
                color: #eee;
                font-size: 1rem;
                height: 2rem;
                margin: 0.4rem 0;
                outline: none;
                padding: 0.5rem;
                width: 100%;
              }
              
              #post-input {
                align-items: center;
                border: 1px solid #444;
                box-sizing: border-box;
                display: flex;
                flex-direction: column;
                justify-content: right;
                position: relative;
                width: 100%;
              }
              
              #post-content {
                background-color: #111;
                border: none;
                box-sizing: border-box;
                color: #eee;
                font-size: 1.2rem;
                height: 5rem;
                outline: none;
                padding: 0.5rem;
                resize: none;
                width: 100%;
              }
              
              #send-button {
                background-color: #ff0000;;
                border: none;
                border-radius: 9999px;
                color: #eee;
                cursor: pointer;
                font-size: 0.9rem;
                font-weight: 700;
                padding: 6px 14px;
                position: absolute;
                right: 2%;
                text-align: center;
                text-decoration: none;
                top: 50%;
                transition: background-color 0.3s ease-out;
              }
              
              #add-button:hover {
                background-color: #cc0000;
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
            </style>
            
            <div id="main-board">
                    <input id="search-input" type="text" placeholder="Search">
                    <div id="post-input">
                        <textarea id="post-content" placeholder="What's happening?"></textarea>
                        <button id="send-button">SEND</button>
                    </div>
                    <ul id="data-list"></ul>
            </div>
        `;
    }
}

customElements.define('home-modal', Home)