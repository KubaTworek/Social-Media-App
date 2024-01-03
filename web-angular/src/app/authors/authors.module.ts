import {NgModule} from "@angular/core";

import {RouterModule} from "@angular/router";
import {AuthorsComponent} from "./authors.component";
import {AuthorsListComponent} from "./authors-list/authors-list.component";
import {AuthorCardComponent} from "./authors-list/author-card/author-card.component";
import {SharedModule} from "../shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import {AuthorActivitiesComponent} from "./author-activities/author-activities.component";
import {AuthorActivitiesResolverService} from "./service/author-activities-resolver.service";

@NgModule({
  declarations: [
    AuthorsComponent,
    AuthorsListComponent,
    AuthorCardComponent,
    AuthorActivitiesComponent
  ],
  imports: [
    SharedModule,
    RouterModule.forChild([
      {path: 'following', component: AuthorsComponent},
      {path: 'followers', component: AuthorsComponent},
      {path: ':id', component: AuthorActivitiesComponent, resolve: [AuthorActivitiesResolverService]}
    ]),
    TranslateModule
  ]
})
export class AuthorsModule {
}
