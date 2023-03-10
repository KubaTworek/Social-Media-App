import {ModalFactory} from "./modal-factory.js";

export class Authorization extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: "open"});
        this.shadowRoot.innerHTML = this.render();
    }

    connectedCallback() {
        this.shadowRoot
            .getElementById("login-button")
            .addEventListener("click", () => this.createModal("login"));
        this.shadowRoot
            .getElementById("logout-button")
            .addEventListener("click", () => this.createModal("logout"));
        this.shadowRoot
            .getElementById("register-button")
            .addEventListener("click", () => this.createModal("registration"));
    }

    createModal(type) {
        const modal = ModalFactory.create(type);
        this.shadowRoot.appendChild(modal);
        modal.open();
    }

    render() {
        return `
            <style>
              .button {
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
              }
              .button:hover {
                background-color: #cc0000;
              }
            </style>
            
            <button class="button" id="login-button">LOGIN</button>
            <button class="button" id="logout-button">LOGOUT</button>
            <button class="button" id="register-button">REGISTER</button>
    `;
    }
}

customElements.define('authorization-modal', Authorization);