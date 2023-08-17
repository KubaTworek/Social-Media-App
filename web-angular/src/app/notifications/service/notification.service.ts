import {Injectable} from '@angular/core';
import {Notification} from '../dto/notification.type';
import {Subject} from "rxjs";


@Injectable()
export class NotificationService {

  notificationsChanged = new Subject<Notification[]>();
  private notifications: Notification[] = [];

  constructor() {
  }

  setNotifications(notifications: Notification[]) {
    this.notifications = notifications;
    this.notificationsChanged.next(this.notifications.slice());
  }

  getNotifications() {
    return this.notifications.slice();
  }
}
