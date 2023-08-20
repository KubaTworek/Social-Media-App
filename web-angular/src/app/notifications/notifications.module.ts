import {NgModule} from "@angular/core";

import {RouterModule} from "@angular/router";
import {NotificationsComponent} from "./notifications.component";
import {NotificationListComponent} from "./notification-list/notification-list.component";
import {NotificationCardComponent} from "./notification-list/notification-card/notification-card.component";
import {SharedModule} from "../shared/shared.module";

@NgModule({
  declarations: [
    NotificationsComponent,
    NotificationListComponent,
    NotificationCardComponent
  ],
  imports: [
    SharedModule,
    RouterModule.forChild([
      {path: '', component: NotificationsComponent},
    ])
  ]
})
export class NotificationsModule {
}
