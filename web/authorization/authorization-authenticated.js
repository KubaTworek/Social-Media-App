import {Authorization} from "./authorization-modal.js";
import {Http} from "../config/http.js";
import {config} from "../config/config.js";

export class AuthorizationAuthenticated extends Authorization {

    constructor() {
        super();
        this.getUser();
    }

    async getUser() {
        const user = await this.getContext();
        this.shadowRoot.innerHTML = this.render(user);
        this.shadowRoot
            .getElementById("logout-button")
            .addEventListener("click", () => this.createModal("logout"));
    }

    async getContext() {
        const headers = {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Authorization": sessionStorage.getItem("jwt")
        };
        return await Http.getInstance().doUserInfo(config.authorizationUrl + "user-info", headers);
    }

    render(user) {
        return `
            <style>
              section {
                display: flex;
                flex-direction: column;
                align-items: center;
                margin: 0 0.5rem;
              }
              
              p {
                margin: 0.5rem auto;
                color: white;
                font-size: 1.2rem;
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
              }
              button:hover {
                background-color: #cc0000;
              }
            </style>
            
            <section> 
                <p>Hello, ${user.firstName} ${user.lastName}</p>
                <button id="logout-button">LOGOUT</button>   
            </section>
    `;
    }
}

customElements.define('authorization-authenticated', AuthorizationAuthenticated);
