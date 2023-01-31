import {ArticleModal} from "../article/article-modal.js"
import {ArticlePost} from "../article/article-post.js"
import {ArticleForm} from "../article/article-form.js"
import Navigo from '/web/node_modules/navigo/src/index.js';
import {AuthorModal} from "../author/author-modal.js";
import {MagazineModal} from "../magazine/magazine-modal.js";
import {AuthorPost} from "../author/author-post.js";
import {AuthorForm} from "../author/author-form.js";
import {MagazinePost} from "../magazine/magazine-post.js";
import {MagazineForm} from "../magazine/magazine-form.js";


export class RouterHandler {
    constructor() {
        if (!RouterHandler.instance) {
            RouterHandler.instance = this;
        } else {
            throw new Error('use getInstance');
        }

        var root = null;
        var useHash = true;
        var hash = '#';
        this.router = new Navigo(root, useHash, hash);
        return RouterHandler.instance;
    }

    static get getInstance() {
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
        const routes = [
            {path: '/articles', resolve:ArticleModal},
            {path: '/authors', resolve:AuthorModal},
            {path: '/magazines', resolve:MagazineModal},
        ];

        this.router.on(() => {
            RouterHandler.inject(new ArticleModal())
        }).resolve();

        routes.forEach(route => {
            this.router.on(
                route.path,
                (params) => {
                    RouterHandler.inject(new route.resolve(params))
                }
            ).resolve();
        });

    }
}
RouterHandler.instance = null;