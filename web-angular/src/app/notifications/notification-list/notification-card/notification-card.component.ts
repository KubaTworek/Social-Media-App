import {Component, Input, ViewEncapsulation} from "@angular/core";
import {Notification} from "../../dto/notification.type";

@Component({
  selector: 'notification-card',
  templateUrl: './notification-card.component.html',
  styleUrls: ['./notification-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NotificationCardComponent {
  @Input() notification!: Notification

  constructor() {
  }
}
