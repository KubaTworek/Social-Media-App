import Navigo from 'navigo';
import {Home} from "../home/home.js";

export class RouterHandler {
    static instance = null;

    constructor() {
        const root = '/';
        const useHash = true;
        const hash = '#';
        this.router = new Navigo(root, useHash, hash);
    }

    static getInstance() {
        if (!RouterHandler.instance) {
            RouterHandler.instance = new RouterHandler();
        }
        return RouterHandler.instance;
    }

    static inject(component) {
        const outlet = document.querySelector('main');
        while (outlet.firstChild) {
            outlet.removeChild(outlet.firstChild);
        }
        outlet.appendChild(component);
    }

    init() {
        this.router.on(() => {
            RouterHandler.inject(new Home());
        }).resolve();

        const routes = [
            {path: '/home', component: Home}
        ];

        routes.forEach(route => {
            this.router.on(route.path, params => {
                RouterHandler.inject(new route.component(params));
            }).resolve();
        });
    }
}
