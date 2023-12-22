import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {NotificationService} from "./notification.service";
import {Notification} from "../dto/notification.type";
import {AuthorizationService} from "../../auth/service/authorization.service";

@Injectable({providedIn: 'root'})
export class NotificationsResolverService implements Resolve<Notification[]> {
  constructor(
    private dataStorageService: DataStorageService,
    private notificationService: NotificationService,
    private authorizationService: AuthorizationService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const notifications = this.notificationService.getNotifications();
    console.log(notifications)
    if (notifications.length === 0) {
      if (this.authorizationService.getRole() == "ROLE_ADMIN") {
        return this.dataStorageService.fetchNotificationsAdmin();
      } else {
        return this.dataStorageService.fetchNotifications();
      }
    } else {
      return notifications;
    }
  }
}
