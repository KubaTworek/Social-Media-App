import {NgModule} from "@angular/core";

import {RouterModule} from "@angular/router";
import {AuthorsComponent} from "./authors.component";
import {AuthorsListComponent} from "./authors-list/authors-list.component";
import {AuthorCardComponent} from "./authors-list/author-card/author-card.component";
import {SharedModule} from "../shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
  declarations: [
    AuthorsComponent,
    AuthorsListComponent,
    AuthorCardComponent
  ],
  imports: [
    SharedModule,
    RouterModule.forChild([
      {path: 'following', component: AuthorsComponent},
      {path: 'followers', component: AuthorsComponent},
    ]),
    TranslateModule
  ]
})
export class AuthorsModule {
}
