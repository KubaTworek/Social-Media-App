import {Component, OnInit} from '@angular/core';
import {NotificationService} from "../notification.service";
import {Notification} from "../dto/notification.type";

@Component({
  selector: 'notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent implements OnInit {
  notificationList: Notification[] = [];

  constructor(private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.getNotifications();
  }

  async getNotifications(): Promise<void> {
    try {
      this.notificationList = await this.notificationService.getNotifications() || [];
      console.log(this.notificationList);
    } catch (error) {
      console.error(error);
    }
  }
}
