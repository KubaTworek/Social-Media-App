export class NotificationCard extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'})
    }

    set setNotification(notification) {
        this.notification = notification;
        this.render();
    }

    render() {
        const {name, message, content} = this.notification || {};

        this.shadowRoot.innerHTML = `
            <style>
              .notification-card {
                background-color: #111;
                border-bottom: 1px solid #444;
                border-top: none;
                box-sizing: border-box;
                color: #eee;
                display: block;
                padding: 0 0.8rem;
              }
              
              .message {
                text-align: left;
              }
              
              .name {
                font-weight: 700;
                text-align: left;
              }
              
              .content {
                text-align: justify;
                color: #666;
              }
            </style>
    
            <div class="notification-card">
                <p class="message"><span class="name">${name}</span> ${message}</p>
                <p class="content">${content}</p>
            </div>
        `;

        this.articleCard = this.shadowRoot.querySelector('.notification-card');
    }
}

customElements.define('notification-card', NotificationCard)