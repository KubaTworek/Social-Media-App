import {NgModule} from "@angular/core";
import {ArticlesComponent} from "./articles.component";
import {ArticlePostComponent} from "./article-post/article-post.component";
import {ArticleListComponent} from "./article-list/article-list.component";
import {ArticleCardComponent} from "./article-list/article-card/article-card.component";
import {ArticleDeleteComponent} from "./article-list/article-card/article-delete/article-delete.component";
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {SharedModule} from "../shared/shared.module";
import {ArticleDetailsComponent} from "./article-list/article-card/article-details/article-details.component";
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
  declarations: [
    ArticlesComponent,
    ArticlePostComponent,
    ArticleListComponent,
    ArticleCardComponent,
    ArticleDeleteComponent,
    ArticleDetailsComponent
  ],
  imports: [
    FormsModule,
    SharedModule,
    RouterModule.forChild([
      {path: '', component: ArticlesComponent},
    ]),
    TranslateModule
  ]
})
export class ArticlesModule {
}
