import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {AuthorsService} from "./authors.service";
import {AuthorWithActivities} from "../dto/author-with-activities.type";

@Injectable({providedIn: 'root'})
export class AuthorActivitiesResolverService implements Resolve<AuthorWithActivities> {
  constructor(
    private dataStorageService: DataStorageService,
    private authorsService: AuthorsService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): AuthorWithActivities {
    const authorId = route.paramMap.get('id');
    const author = this.authorsService.getAuthorWithActivities();

    if (authorId && authorId !== author.id) {
      this.dataStorageService.fetchAuthorActivities(authorId)
    }

    return author;
  }
}
