import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {RegisterComponent} from "./register/register.component";
import {LoginComponent} from "./login/login.component";
import {SharedModule} from "../shared/shared.module";

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent
  ],
  imports: [
    SharedModule,
    RouterModule.forChild([
      {path: 'login', component: LoginComponent},
    ]),
    RouterModule.forChild([
      {path: 'register', component: RegisterComponent},
    ])
  ]
})
export class AuthModule {
}
