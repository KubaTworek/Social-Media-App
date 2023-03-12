import {ModalForm} from "../modal/modal-form.js";

export class AuthorizationForm extends ModalForm {
    connectedCallback() {
        super.connectedCallback();
        this.shadowRoot
            .getElementById("submit-button")
            .addEventListener("click", this.submit.bind(this));
    }

    async submit(event) {
        event.preventDefault();
        this.authorize()
        this.hide()
    }

    getData(requiredFields) {
        const formData = {};
        for (const fieldName of requiredFields) {
            const inputElement = this.shadowRoot.getElementById(fieldName);
            if (inputElement) {
                formData[fieldName] = inputElement.value;
            }
        }
        return formData;
    }
}