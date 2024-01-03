// notification-list.component.ts
import {Component, OnDestroy, OnInit} from '@angular/core';
import {NotificationService} from "../service/notification.service";
import {Notification} from "../dto/notification.type";
import {Subscription} from "rxjs";

@Component({
  selector: 'notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent implements OnInit, OnDestroy {
  notifications: Notification[] = [];
  private notificationsSubscription: Subscription = new Subscription();

  constructor(
    private notificationService: NotificationService
  ) {
  }

  ngOnInit(): void {
    this.notificationsSubscription = this.notificationService.notificationsChanged$
      .subscribe(
        (notifications: Notification[]) => {
          this.notifications = notifications;
        }
      );
  }

  ngOnDestroy(): void {
    this.notificationsSubscription.unsubscribe();
  }
}
