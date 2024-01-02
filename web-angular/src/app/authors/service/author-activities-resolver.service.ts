import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {AuthorsService} from "./authors.service";
import {Author} from "../dto/author.type";
import {AuthorWithActivities} from "../dto/author-with-activities.type";

@Injectable({providedIn: 'root'})
export class AuthorActivitiesResolverService implements Resolve<AuthorWithActivities> {
  constructor(
    private dataStorageService: DataStorageService,
    private authorsService: AuthorsService
  ) {
  }

  // @ts-ignore
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const authorId = route.paramMap.get('id');
    console.log(authorId)
    //const author = this.authorsService.getAuthorWithActivities()

    if (authorId != null) {
      const xd = this.dataStorageService.fetchAuthorActivities(authorId);
      console.log(xd)
      const author = this.authorsService.getAuthorWithActivities()
      console.log(author)
      return xd;
    }
    return null;
  }
}
