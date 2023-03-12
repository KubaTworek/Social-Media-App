import {AuthorizationAnonymous} from "./authorization-anonymous.js";
import {AuthorizationAuthenticated} from "./authorization-authenticated.js";

export class AuthorizationFactory {
    static create() {
        if (sessionStorage.getItem("jwt") == null) {
            return new AuthorizationAnonymous();
        } else {
            return new AuthorizationAuthenticated();
        }
    }
}