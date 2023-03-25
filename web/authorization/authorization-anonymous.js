import {Authorization} from "./authorization-modal.js";

export class AuthorizationAnonymous extends Authorization {

    constructor() {
        super();
    }

    connectedCallback() {
        this.shadowRoot.innerHTML = this.render();

        this.shadowRoot
            .getElementById("login-button")
            .addEventListener("click", () => this.createModal("login"));
        this.shadowRoot
            .getElementById("register-button")
            .addEventListener("click", () => this.createModal("registration"));
    }

    render() {
        return `
            <style>
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
              }
              button:hover {
                background-color: #cc0000;
              }
            </style>
            
            <button id="login-button">LOGIN</button>
            <button id="register-button">REGISTER</button>
    `;
    }
}

customElements.define('authorization-anonymous', AuthorizationAnonymous);
