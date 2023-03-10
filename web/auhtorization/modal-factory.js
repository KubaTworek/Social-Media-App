import {LoginForm} from "./login-form.js";
import {LogoutForm} from "./logout-form.js";
import {RegistrationForm} from "./register-form.js";

export class ModalFactory {
    static create(type) {
        switch (type) {
            case "login":
                return new LoginForm();
            case "logout":
                return new LogoutForm();
            case "registration":
                return new RegistrationForm();
            default:
                throw new Error("Invalid modal type");
        }
    }
}