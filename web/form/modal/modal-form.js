export class ModalForm extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: "open"});
    }

    connectedCallback() {
        this.shadowRoot.innerHTML = this.render();

        this.shadowRoot
            .addEventListener("click", this._handleBackdropClick);
        this.shadowRoot
            .getElementById("cancel-button")
            .addEventListener("click", this.cancel.bind(this));
    }

    open() {
        this.setAttribute("opened", "");
    }

    hide() {
        this.removeAttribute("opened");
    }

    cancel(event) {
        this.hide();
        const cancelEvent = new Event("cancel", {bubbles: true, composed: true});
        event.target.dispatchEvent(cancelEvent);
    }

    _handleBackdropClick = (event) => {
        if (event.target.id === "backdrop") {
            this.cancel();
        }
    };
}