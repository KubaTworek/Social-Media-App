import {RouterHandler} from "./router/router-handler.js";
import {Http} from "./http/http.js";

class App {
    constructor() {
        const router = new RouterHandler();
        new Http();
        router.init();
    }
}

new App()