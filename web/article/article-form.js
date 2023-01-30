export class ArticleForm extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
        this.shadowRoot.innerHTML = this.render()
    }

    connectedCallback() {
        const backdrop = this.shadowRoot.getElementById('backdrop');
        const cancelButton = this.shadowRoot.getElementById('cancel-button');
        const form = this.shadowRoot.getElementById('form-container');

        backdrop.addEventListener('click', this._cancel.bind(this));
        cancelButton.addEventListener('click', this._cancel.bind(this));
        form.addEventListener('submit', (e) => {
            e.preventDefault()
            const preArticle = new FormData(form)
            let articleArr = []
            for (let [k, v] of preArticle.entries()) {
                articleArr.push(v);
            }
            this.postData(articleArr)
        })
    }

    open() {
        this.setAttribute('opened', '');
    }

    hide() {
        if (this.hasAttribute('opened')) {
            this.removeAttribute('opened');
        }
    }

    _cancel(event) {
        this.hide();
        const cancelEvent = new Event('cancel', {bubbles: true, composed: true});
        event.target.dispatchEvent(cancelEvent);
    }

    postData(arr){
        fetch('http://localhost:8887/articles', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: arr[0],
                text: arr[1],
                magazine: arr[2],
                author_firstName: arr[3],
                author_lastName: arr[4]
            })
        }).catch(err => console.log(err))
        this.hide()
    }

    render() {
        return `
        <style>
            #backdrop {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100vh;
                background: rgba(0,0,0,0.75);
                z-index: 10;
                opacity: 0;
                pointer-events: none;
            }

            :host([opened]) #backdrop,
            :host([opened]) #background {
                opacity: 1;
                pointer-events: all;
            }

            :host([opened]) #background {
                top: 15vh;
            }

            #background {
                position: fixed;
                top: 10vh;
                left: 25%;
                width: 50%;
                z-index: 100;
                background: white;
                border-radius: 3px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.26);
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                opacity: 0;
                pointer-events: none;
                transition: all 0.3s ease-out;
            }

            header {
                padding: 1rem;
                border-bottom: 1px solid #ccc;
            }

            ::slotted(h1) {
                font-size: 1.25rem;
                margin: 0;
            }

            #content {
                padding: 1rem;
            }

            #buttons-container {
                border-top: 1px solid #ccc;
                padding: 1rem;
                display: flex;
                justify-content: flex-end;
            }

            #buttons-container button {
                margin: 0 0.25rem;
            }
        </style>
        
        <div id="backdrop"></div>
        <div id="background">
            <header>
                <h2 id="title">Article</h2>
            </header>
            <section id="content">
                <form id="form-container">
                    <input name="title" type="text">
                    <input name="text" type="text">
                    <input name="magazine" type="text">
                    <input name="author_firstName" type="text">
                    <input name="author_lastName" type="text">
                    <button type="submit">Add</button>
                </form>
            </section>
            <section id="buttons-container">
                <button id="cancel-button">Cancel</button>
            </section>
        </div>
    `;
    }
}

customElements.define('article-form', ArticleForm);