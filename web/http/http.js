export class Http {
    static instance = null;

    constructor() {
        if (!Http.instance) {
            Http.instance = this;
        }

        return Http.instance;
    }

    static getInstance() {
        return Http.instance || new Http();
    }

    headers = {
        Accept: "application/json",
        "Content-Type": "application/json",
    };

    doGet = (path) =>
        fetch(`${path}`, { headers: this.headers })
            .then((r) => r.json())
            .catch((err) => console.log(err));

    doPost = (path, body) =>
        fetch(`${path}`, {
            headers: this.headers,
            method: "POST",
            body,
        }).catch((err) => console.log(err));

    doDelete = (path) =>
        fetch(`${path}`, {
            headers: this.headers,
            method: "DELETE",
        }).catch((err) => console.log(err));
}
