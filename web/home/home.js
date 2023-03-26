import {ArticleCard} from "./article-card.js";
import {Http} from '../config/http.js';
import {config} from "../config/config.js";
import {ArticlePost} from "./article-post.js";
import {AuthorizationFactory} from "../authorization/authorization-factory.js";
import {NotificationCard} from "./notification-card.js";

export class Home extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    connectedCallback() {
        this.shadowRoot.innerHTML = this.render();

        this.dataList = this.shadowRoot.getElementById('data-list');
        this.input = this.shadowRoot.getElementById('search-input');
        this.authorizationBoard = this.shadowRoot.getElementById('authorization-board');
        this.authorizationBoard.appendChild(AuthorizationFactory.create())
        this.inputBar = this.shadowRoot.getElementById('input-bar');
        this.inputBar.appendChild(new ArticlePost());

        this.shadowRoot.getElementById('search-input')
            .addEventListener('keyup', (event) => {
                this.getData(event);
            });
        this.shadowRoot.getElementById('home-button')
            .addEventListener('click', () => {
                this.getData()
            });
        this.shadowRoot.getElementById('notifications-button')
            .addEventListener('click', () => {
                this.getNotificationsData()
            });
        this.getData()
    }

    async getData(event) {
        if (event && event.key !== 'Enter') {
            return;
        }
        this.inputBar.style.display = 'block';
        this.dataList.innerHTML = '';
        const articles = await this.getArticles();
        this.renderArticles(articles);
        this.input.value = '';
    }

    async getNotificationsData() {
        this.inputBar.style.display = 'none';
        this.dataList.innerHTML = '';
        const notifications = await this.getNotifications();
        this.renderNotifications(notifications);
    }

    async getArticles() {
        return await Http.getInstance().doGet(config.articlesUrl + this.input.value);
    }

    async getNotifications() {
        const headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': sessionStorage.getItem('jwt'),
        };
        return await Http.getInstance().doGetWithHeaders(config.notificationsUrl, headers);
    }

    renderArticles(articles) {
        articles.forEach((article) => {
            const li = document.createElement('li');
            const el = new ArticleCard();
            el.setArticle = article;
            li.appendChild(el);
            this.dataList.appendChild(li);
        });
    }

    renderNotifications(notifications) {
        notifications.forEach((notification) => {
            const li = document.createElement('li');
            const el = new NotificationCard();
            el.setNotification = notification;
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
              
              button {
                width: 8rem;
                background-color: red;
                border: none;
                color: white;
                padding: 0.5rem 1rem;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1rem;
                margin: 0.5rem;
                cursor: pointer;
                border-radius: 4px;
                transition: background-color 0.3s ease-out;
                bottom: 0;
              }
              
              button:hover {
                background-color: #cc0000;
              }
              
              .button-container {
                display: flex;
                justify-content: center;
                position: fixed;
                bottom: 0;
                width: 100%;
                margin-top: 1rem;
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
                  flex-direction: column-reverse;
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
                
              .button-container {
                  display: flex;
                  align-items: end;
                  flex-direction: column;
                  position: relative;
                  margin-top: 1rem;
                  margin-right: 1.4rem;
                }
              }
            </style>
            <div id="background"> 
              <div id="authorization-board">
                 <div class="button-container">
                  <button id="home-button">Home</button>
                  <button id="notifications-button">Notifications</button>
                </div>
              </div>

              <input id="search-input" type="text" placeholder="Search"> 
              <div id="main-board">
                <div id="input-bar"></div>
                <ul id="data-list"></ul>
              </div>

            </div>
        `;
    }
}

customElements.define('app-home', Home)