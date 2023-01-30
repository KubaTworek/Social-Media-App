import {config} from "../config.js"

export class Http {
    constructor() {
        if(!Http.inst) {
            Http.inst = this
        } else {
            return Http.inst
        }
    }

    static get instance() {
        return Http.inst
    }

    doGet(path) {
        const headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };

        return fetch(config.url + path, {
            headers: headers
        })
            .then(r => r.json())
            .catch(err => console.log(err));
    }

    doPost(path, body) {
        const headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };

        return fetch(config.url + path, {
            headers: headers,
            method: 'POST',
            body: body
        }).catch(err => console.log(err));
    }

    doDelete(path) {
        const headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };

        return fetch(config.url + path, {
            headers: headers,
            method: 'DELETE'
        }).catch(err => console.log(err));
    }
}
Http.inst = null;