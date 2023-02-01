import Navigo from '/web/node_modules/navigo/src/index.js';
import { ArticleModal } from '../article/article-modal.js';
import { AuthorModal } from '../author/author-modal.js';
import { MagazineModal } from '../magazine/magazine-modal.js';

export class RouterHandler {
    static instance = null;

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

    constructor() {
        const root = null;
        const useHash = true;
        const hash = '#';
        this.router = new Navigo(root, useHash, hash);
    }

    init() {
        this.router.on(() => {
            RouterHandler.inject(new ArticleModal());
        }).resolve();

        const routes = [
            { path: '/articles', resolve: ArticleModal },
            { path: '/authors', resolve: AuthorModal },
            { path: '/magazines', resolve: MagazineModal },
        ];

        routes.forEach(route => {
            this.router.on(route.path, params => {
                RouterHandler.inject(new route.resolve(params));
            }).resolve();
        });
    }
}
