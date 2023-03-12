import {Http} from "../../config/http.js";
import {config} from "../../config/config.js";
import {AuthorizationForm} from "./authorization-form.js";

export class RegistrationForm extends AuthorizationForm {
    connectedCallback() {
        super.connectedCallback();
    }

    authorize() {
        const requiredFields = ["username", "password", "firstName", "lastName", "role"];
        const data = this.getData(requiredFields);

        Http.getInstance().doRegister(config.authorizationUrl + "register", JSON.stringify(data))
    }

    render() {
        return `
            <style>
              :host([opened]) #backdrop,
              :host([opened]) #background {
                opacity: 1;
                pointer-events: all;
              }
            
              :host([opened]) #background {
                top: 15vh;
              }
            
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
                justify-content: center;
                align-items: center;
                opacity: 0;
                pointer-events: none;
                transition: all 0.3s ease-out;
              }
            
              form {
                max-width: 400px;
                margin: 50px auto;
                padding: 20px;
                background-color: #fff;
                border-radius: 5px;
                box-shadow: 0 0 10px #ccc;
              }
            
              input[type=text],
              input[type=password] {
                width: 100%;
                padding: 10px;
                margin: 8px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
              }
            
              button {
                width: 100%;
                background-color: #4CAF50;
                color: white;
                padding: 10px 20px;
                margin: 8px 0;
                border: none;
                border-radius: 4px;
                cursor: pointer;
              }
            
              button:hover {
                background-color: #45a049;
              }
              
              #role {
              display: none;
              }
            </style>

        <div id="backdrop"></div>
        <div id="background">
            <form id="register-form">
                <h2>Rejestracja</h2>
                <label for="username">Nazwa użytkownika:</label>
                <input type="text" id="username" name="username" placeholder="Podaj nazwę użytkownika">
        
                <label for="password">Hasło:</label>
                <input type="password" id="password" name="password" placeholder="Podaj hasło">
                
                <label for="confirm_password">Potwierdź hasło:</label>
                <input type="password" id="confirm_password" name="confirm_password" placeholder="Potwierdź hasło">
                
                <label for="firstName">Imię:</label>
                <input type="text" id="firstName" name="firstName" placeholder="Podaj imię">
                            
                <label for="lastName">Nazwisko:</label>
                <input type="text" id="lastName" name="lastName" placeholder="Podaj nazwisko">
                
                <input type="text" id="role" name="role" value="ROLE_USER">
        
                <button id="cancel-button">Cancel</button>
                <button id="submit-button">Ok</button>
            </form>
        </div>
    `;
    }
}

customElements.define('registration-form', RegistrationForm);