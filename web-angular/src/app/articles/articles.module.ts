import {NgModule} from "@angular/core";
import {ArticlesComponent} from "./articles.component";
import {ArticlePostComponent} from "./article-post/article-post.component";
import {ArticleListComponent} from "./article-list/article-list.component";
import {ArticleCardComponent} from "./article-list/article-card/article-card.component";
import {ArticleDeleteComponent} from "./article-comments/article-details/article-delete/article-delete.component";
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {SharedModule} from "../shared/shared.module";
import {ArticleDetailsComponent} from "./article-comments/article-details/article-details.component";
import {TranslateModule} from "@ngx-translate/core";
import {ArticleResolverService} from "./service/article-resolver.service";
import {ArticleCommentsComponent} from "./article-comments/article-comments.component";


@NgModule({
  declarations: [
    ArticlesComponent,
    ArticlePostComponent,
    ArticleListComponent,
    ArticleCardComponent,
    ArticleDeleteComponent,
    ArticleDetailsComponent,
    ArticleCommentsComponent
  ],
  imports: [
    FormsModule,
    SharedModule,
    RouterModule.forChild([
      {path: '', component: ArticlesComponent},
      {path: ':id', component: ArticleCommentsComponent, resolve: [ArticleResolverService]}
    ]),
    TranslateModule
  ]
})
export class ArticlesModule {
}
