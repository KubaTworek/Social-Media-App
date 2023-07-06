import {Http} from "../config/http.js";
import {ModalForm} from "./modal/modal-form.js";

export class DeleteForm extends ModalForm {
    constructor(text, url) {
        super();
        this.text = text;
        this.url = url;
    }

    connectedCallback() {
        super.connectedCallback();
        this.shadowRoot.innerHTML = this.render()
        this.shadowRoot.getElementById("confirm-button")
            .addEventListener("click", this.delete.bind(this));
    }

    delete() {
        const headers = {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Authorization": sessionStorage.getItem("jwt")
        };
        Http.getInstance()
            .doDelete(this.url, headers)
            .then(() => location.reload())
        this.hide();
    }

    render() {
        return `
            <style>
              #backdrop {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100vh;
                background: rgba(0, 0, 0, 0.75);
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
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.26);
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
        
              #buttons-container button:not(:last-child) {
                margin-right: 0.25rem;
              }
              
              #content p {
                font-size: 1.5rem;
                color: #333;
              }
            </style>
        
            <div id="backdrop"></div>
            <div id="background">
              <section id="content">
                <p>${this.text}</p>
              </section>
              <section id="buttons-container">
                <button id="cancel-button">Cancel</button>
                <button id="confirm-button">Ok</button>
              </section>
            </div>
    `;
    }

}

customElements.define('article-delete', DeleteForm);