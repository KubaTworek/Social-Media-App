import {Http} from "../http/http.js";

export class DeletePopup extends HTMLElement {
    constructor(text, url) {
        super();
        this.attachShadow({mode: 'open'})
        this.shadowRoot.innerHTML = this.render(text)
        this.url = url
    }

    connectedCallback() {
        const cancelButton = this.shadowRoot.getElementById('cancel-button');
        const confirmButton = this.shadowRoot.getElementById('confirm-button');

        cancelButton.addEventListener('click', this._cancel.bind(this));
        confirmButton.addEventListener('click', this._delete.bind(this));
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

    _delete() {
        this.hide();
        Http.instance.doDelete(this.url)
            .then(() => location.reload())
            .catch(err => console.log(err))
    }

    render(text) {
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
            <section id="content">
            <p>${text}</p>
            </section>
            <section id="buttons-container">
                <button id="cancel-button">Cancel</button>
                <button id="confirm-button">Ok</button>
            </section>
        </div>
    `;
    }
}

customElements.define('delete-popup', DeletePopup);