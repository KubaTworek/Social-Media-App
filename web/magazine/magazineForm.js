export class MagazineForm extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
        this.isOpen = false
        this.shadowRoot.innerHTML = `
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
            :host([opened]) #modal {
                opacity: 1;
                pointer-events: all;
            }

            :host([opened]) #modal {
                top: 15vh;
            }

            #modal {
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

            #main {
                padding: 1rem;
            }

            #actions {
                border-top: 1px solid #ccc;
                padding: 1rem;
                display: flex;
                justify-content: flex-end;
            }

            #actions button {
                margin: 0 0.25rem;
            }
        </style>
        <div id="backdrop"></div>
        <div id="modal">
            <header>
                <h2 id="title">Magazine</h2>
            </header>
            <section id="main">
                <form id="form">
                    <input name="name" type="text">
                    <button type="submit">Add</button>
                </form>
            </section>
            <section id="actions">
                <button id="cancel-btn">Cancel</button>
            </section>
        </div>
    `;
        const backdrop = this.shadowRoot.querySelector('#backdrop');
        const cancelButton = this.shadowRoot.querySelector('#cancel-btn');
        const form = this.shadowRoot.querySelector('#form');

        backdrop.addEventListener('click', this._cancel.bind(this));
        cancelButton.addEventListener('click', this._cancel.bind(this));
        form.addEventListener('submit', (e) => {
            e.preventDefault()

            const preMagazine = new FormData(form)
            let magazineArr = []
            for (let [k, v] of preMagazine.entries()) {
                magazineArr.push(v);
            }

            fetch('http://localhost:8887/magazines', {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    magazineName: magazineArr[0],
                    test: 'test'
                })
            }).then(r => r.json())
                .then(data => console.log(data))
                .catch(err => console.log(err))
            this.hide();
        })
    }

    open() {
        this.setAttribute('opened', '');
        this.isOpen = true;
    }

    hide() {
        if (this.hasAttribute('opened')) {
            this.removeAttribute('opened');
        }
        this.isOpen = false;
    }

    _cancel(event) {
        this.hide();
        const cancelEvent = new Event('cancel', {bubbles: true, composed: true});
        event.target.dispatchEvent(cancelEvent);
    }
}

customElements.define('magazine-form', MagazineForm);