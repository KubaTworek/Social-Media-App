import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {NotificationService} from "./notification.service";
import {Notification} from "../dto/notification.type";

@Injectable({providedIn: 'root'})
export class NotificationsResolverService implements Resolve<Notification[]> {
  constructor(
    private dataStorageService: DataStorageService,
    private notificationService: NotificationService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const notifications = this.notificationService.getNotifications();

    if (notifications.length === 0) {
      return this.dataStorageService.fetchNotifications();
    } else {
      return notifications;
    }
  }
}
