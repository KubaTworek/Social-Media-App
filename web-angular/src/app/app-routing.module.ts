import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotificationsComponent} from "./notifications/notifications.component";
import {ArticlesComponent} from "./articles/articles.component";

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: ArticlesComponent},
  {path: 'notifications', component: NotificationsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
