export class Http {
    static instance = null;
    headers = {
        "Accept": "application/json",
        "Content-Type": "application/json",
    };

    constructor() {
        if (!Http.instance) {
            Http.instance = this;
        }

        return Http.instance;
    }

    static getInstance() {
        return Http.instance || new Http();
    }

    doGet = (path) =>
        fetch(`${path}`, {
            headers: this.headers,
        }).then((r) => r.json())
            .catch((err) => console.log(err));

    doGetWithHeaders = (path, headers) =>
        fetch(`${path}`, {
            headers: headers
        }).then((r) => r.json())
            .catch((err) => console.log(err));

    doPost = (path, body, headers) =>
        fetch(`${path}`, {
            headers: headers,
            method: "POST",
            body,
        }).then((r) => r.json()).catch((err) => console.log(err));

    doDelete = (path, headers) =>
        fetch(`${path}`, {
            headers: headers,
            method: "DELETE",
        }).catch((err) => console.log(err));

    doLogin = (path, body) =>
        fetch(`${path}`, {
            method: "POST",
            headers: this.headers,
            body: body,
        }).then((r) => r.json())
            .catch((err) => console.log(err));

    doRegister = (path, body) =>
        fetch(`${path}`, {
            method: "POST",
            headers: this.headers,
            body: body,
        }).catch((err) => alert(err));

    doUserInfo = (path, headers) =>
        fetch(`${path}`, {
            headers: headers
        }).then((r) => r.json()).catch((err) => alert(err));
}
