import {Component, OnInit} from '@angular/core';
import {NotificationService} from "../service/notification.service";
import {Notification} from "../dto/notification.type";
import {Subscription} from "rxjs";

@Component({
  selector: 'notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent implements OnInit {
  notificationList: Notification[] = [];
  private subscription: Subscription = new Subscription();

  constructor(private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.subscription = this.notificationService.getNotifications()
      .subscribe(
        (notifications: Notification[]) => {
          this.notificationList = notifications || [];
        },
        (error) => {
          console.error(error);
        }
      );
  }
}
