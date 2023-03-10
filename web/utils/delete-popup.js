import {Http} from "../http/http.js";

export class DeletePopup extends HTMLElement {
    constructor(text, url) {
        super();
        this.attachShadow({ mode: "open" });
        this.shadowRoot.innerHTML = this.render(text);
        this.url = url;
    }

    connectedCallback() {
        this.shadowRoot.addEventListener("click", this._handleBackdropClick);
        this.shadowRoot.getElementById("cancel-button")
            .addEventListener("click", this.cancel.bind(this));
        this.shadowRoot.getElementById("confirm-button")
            .addEventListener("click", this.delete.bind(this));
    }

    open() {
        this.setAttribute("opened", "");
    }

    hide() {
        this.removeAttribute("opened");
    }

    cancel(event) {
        this.hide();
        const cancelEvent = new Event("cancel", { bubbles: true, composed: true });
        event.target.dispatchEvent(cancelEvent);
    }

    _handleBackdropClick = (event) => {
        if (event.target.id === "backdrop") {
            this.cancel();
        }
    };

    delete() {
        this.hide();
        sessionStorage.setItem("jwt", "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb2NpYWwgTWVkaWEiLCJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VybmFtZSI6ImhhcHB5WCIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY3ODQ0MjQzMywiZXhwIjoxNjc4NDUzMjAzfQ.5wrFWn7_nG5XOfAzgf-Qh1V1OQD2HJKf5utI2hCNzlU")
        const headers = {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Authorization": sessionStorage.getItem("jwt")
        };
        Http.getInstance().doDelete(this.url, headers)
            .then(() => location.reload())
            .catch((err) => console.error(err));
    }

    render(text) {
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
            <section id="content">
            <p>${text}</p>
            </section>
            <section id="buttons-container">
                <button id="cancel-button">Cancel</button>
                <button id="confirm-button">Ok</button>
            </section>
        </div>
    `;
    }
}

customElements.define('delete-popup', DeletePopup);