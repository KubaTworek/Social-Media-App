import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {Author} from '../dto/author.type';
import {AuthorizationService} from '../../auth/service/authorization.service';
import {TranslateService} from '@ngx-translate/core';
import {AuthorWithActivities} from "../dto/author-with-activities.type";

@Injectable()
export class AuthorsService {

  authorsChanged = new Subject<Author[]>();
  messageChanged = new Subject<string>();
  activityChanged = new Subject<AuthorWithActivities>();
  private authors: Author[] = [];
  private author: AuthorWithActivities = new AuthorWithActivities("", "", "", "", [], [], []);
  private message: string = "";

  constructor(
    private authorizationService: AuthorizationService,
    private translateService: TranslateService,
  ) {
  }

  setAuthors(authors: Author[], author: Author, msg: string) {
    const lang = this.translateService.currentLang;
    this.setMessage(lang, author, msg);
    this.authors = authors;
    this.notifyChanges();
  }

  setAuthorWithActivities(author: AuthorWithActivities) {
    this.author = author;
    this.notifyChangesActivities();
  }

  getAuthors(): Author[] {
    return [...this.authors];
  }

  getAuthorWithActivities(): AuthorWithActivities {
    return this.author;
  }

  getMessage(): string {
    return this.message;
  }

  private setMessage(lang: string, author: Author, msg: string) {
    const isCurrentUser = this.authorizationService.getUsername() === author.username;

    if (lang === 'pl') {
      this.message = isCurrentUser ? this.getPlMessageCurrentUser(msg) : this.getPlMessageOtherUser(author, msg);
    } else {
      this.message = isCurrentUser ? this.getEngMessageCurrentUser(msg) : this.getEngMessageOtherUser(author, msg);
    }
  }

  private getPlMessageCurrentUser(msg: string): string {
    return msg === 'followers' ? 'Obserwują Cię:' : 'Obserwujesz:';
  }

  private getPlMessageOtherUser(author: Author, msg: string): string {
    return msg === 'followers'
      ? `${author.firstName} ${author.lastName} jest obserwowany/a przez:`
      : `${author.firstName} ${author.lastName} obserwuje:`;
  }

  private getEngMessageCurrentUser(msg: string): string {
    return msg === 'followers' ? 'Your followers:' : 'You are following:';
  }

  private getEngMessageOtherUser(author: Author, msg: string): string {
    return msg === 'followers'
      ? `${author.firstName} ${author.lastName}'s followers:`
      : `${author.firstName} ${author.lastName} is following:`;
  }

  private notifyChanges() {
    this.authorsChanged.next([...this.authors]);
    this.messageChanged.next(this.message);
  }

  private notifyChangesActivities() {
    this.activityChanged.next(this.author);
  }
}
