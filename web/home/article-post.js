import {Http} from "../http/http.js";
import {config} from "../config.js";

export class ArticlePost extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: "open"});
        this.shadowRoot.innerHTML = this.render();
    }

    connectedCallback() {
        this.shadowRoot.getElementById('send-button')
            .addEventListener('click', this.postData.bind(this))
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

    render() {
        return `
            <style>
                #input-bar {
                    display: grid;
                    grid-template-columns: 1fr auto;
                    align-items: center;
                    justify-content: space-between;
                    width: 80%;
                }
            
                #post-content {
                    background-color: #111;
                    border: none;
                    box-sizing: border-box;
                    color: #eee;
                    font-size: 1rem;
                    height: 100%;
                    outline: none;
                    padding: 0.5rem;
                    resize: none;
                    overflow: hidden;
                    grid-column: 1 / span 2;
                }
            
                #send-button {
                    position: absolute;
                    right: 2%;
                    top:75%;
                    background-color: red;
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
            
                #send-button:hover {
                    background-color: #cc0000;
                }
            
                @media screen and (max-width: 600px) {
                    #input-bar {
                        grid-template-columns: 1fr;
                        grid-template-rows: auto auto;
                    }
                }
            </style>
            
            <div id="input-bar">
                <textarea id="post-content" placeholder="What's happening?"></textarea>
                <button id="send-button">SEND</button>
            </div>
    `;
    }
}

customElements.define('article-post', ArticlePost);