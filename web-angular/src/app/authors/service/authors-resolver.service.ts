import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {AuthorsService} from './authors.service';
import {Author} from '../dto/author.type';

@Injectable({providedIn: 'root'})
export class AuthorsResolverService implements Resolve<Author[]> {
  constructor(
    private authorsService: AuthorsService
  ) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.authorsService.getAuthors();
  }
}
