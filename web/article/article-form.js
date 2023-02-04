import {Http} from "../http/http.js";
import {config} from "../config.js";

export class ArticleForm extends HTMLElement {
    shadowRoot = this.attachShadow({mode: "open"});

    constructor() {
        super();
        this.shadowRoot.innerHTML = this.render();
    }

    connectedCallback() {
        this.shadowRoot.addEventListener("click", this._handleBackdropClick);
        this.shadowRoot.getElementById("cancel-button").addEventListener("click", this._cancel);
        this.shadowRoot.getElementById("form-container").addEventListener("submit", this._submit);
    }

    open() {
        this.setAttribute("opened", "");
    }

    hide() {
        this.removeAttribute("opened");
    }

    _cancel = () => {
        this.hide();
        this.dispatchEvent(new Event("cancel", {bubbles: true, composed: true}));
    };

    _handleBackdropClick = (event) => {
        if (event.target.id === "backdrop") {
            this._cancel();
        }
    };

    _submit = (event) => {
        event.preventDefault();
        const form = this.shadowRoot.getElementById("form-container");
        const data = Object.fromEntries(new FormData(form));
        Http.getInstance().doPost(config.articlesUrl, JSON.stringify(data))
            .then(() => location.reload())
            .catch((err) => console.error(err));
    };

    render() {
        return `
      <style>
        #backdrop {
          position: fixed;
          top: 0;
          left: 0;
          width: 100%;
          height: 100vh;
          background: rgba(0,0,0,0.75);
          z-index: 10;
          opacity: 0;
          pointer-events: none;
        }
        
        :host([opened]) #backdrop,
        :host([opened]) #background {
          opacity: 1;
          pointer-events: all;
        }
        
        :host([opened]) #background {
          top: 15vh;
        }
        
        #background {
          position: fixed;
          top: 10vh;
          left: 25%;
          width: 50%;
          z-index: 100;
          background: white;
          border-radius: 3px;
          box-shadow: 0 2px 8px rgba(0,0,0,0.26);
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          opacity: 0;
          pointer-events: none;
          transition: all 0.3s ease-out;
        }
        
        header {
          padding: 1rem;
          border-bottom: 1px solid #ccc;
        }
        
        ::slotted(h1) {
          font-size: 1.25rem;
          margin: 0;
        }
        
        #content {
          padding: 1rem;
        }
        
        #buttons-container {
          border-top: 1px solid #ccc;
          padding: 1rem;
          display: flex;
          justify-content: flex-end;
        }

        #buttons-container button {
          margin: 0 0.25rem;
        }

        </style>
        
        <div id="backdrop"></div>
        <div id="background">
            <header>
                <h2 id="title">Article</h2>
            </header>
            <section id="content">
                <form id="form-container">
                    <input name="title" type="text">
                    <input name="text" type="text">
                    <input name="magazine" type="text">
                    <input name="author_firstName" type="text">
                    <input name="author_lastName" type="text">
                    <button type="submit">Add</button>
                </form>
            </section>
            <section id="buttons-container">
                <button id="cancel-button">Cancel</button>
            </section>
        </div>
    `;
    }
}

customElements.define('article-form', ArticleForm);