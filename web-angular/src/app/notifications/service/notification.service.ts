// notification.service.ts
import {Injectable} from '@angular/core';
import {Notification} from '../dto/notification.type';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private notificationsChanged = new Subject<Notification[]>();
  notificationsChanged$ = this.notificationsChanged.asObservable();
  private notifications: Notification[] = [];

  constructor() {
  }

  setNotifications(notifications: Notification[]): void {
    this.notifications = notifications;
    this.notificationsChanged.next([...this.notifications]);
  }

  getNotifications(): Notification[] {
    return [...this.notifications];
  }
}
