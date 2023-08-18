import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotificationsComponent} from "./notifications/notifications.component";
import {ArticlesComponent} from "./articles/articles.component";
import {ArticlesResolverService} from "./articles/service/articles-resolver.service";
import {NotificationsResolverService} from "./notifications/service/notifications-resolver.service";
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: ArticlesComponent, resolve: [ArticlesResolverService]},
  {path: 'notifications', component: NotificationsComponent, resolve: [NotificationsResolverService]},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
