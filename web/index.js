import {RouterHandler} from "./config/router-handler.js";

class App {
    constructor() {
        const router = new RouterHandler();
        router.init();
    }
}

new App()