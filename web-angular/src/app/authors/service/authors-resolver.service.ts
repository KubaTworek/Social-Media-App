import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {DataStorageService} from "../../shared/data-storage.service";
import {AuthorsService} from "./authors.service";
import {Author} from "../dto/author.type";

@Injectable({providedIn: 'root'})
export class AuthorsResolverService implements Resolve<Author[]> {
  constructor(
    private dataStorageService: DataStorageService,
    private authorsService: AuthorsService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.authorsService.getAuthors();
  }
}
