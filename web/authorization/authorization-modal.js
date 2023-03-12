import {ModalFactory} from "../form/modal/modal-factory.js";

export class Authorization extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: "open"});
    }

    createModal(type) {
        const modal = ModalFactory.create(type);
        this.shadowRoot.appendChild(modal);
        modal.open();
    }
}
