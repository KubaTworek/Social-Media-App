import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthorsService} from '../service/authors.service';
import {Author} from '../dto/author.type';
import {Subscription} from 'rxjs';

@Component({
  selector: 'authors-list',
  templateUrl: './authors-list.component.html',
  styleUrls: ['./authors-list.component.scss'],
})
export class AuthorsListComponent implements OnInit, OnDestroy {
  authors: Author[] = [];
  message: string = '';
  private authorsSubscription: Subscription = new Subscription();
  private messageSubscription: Subscription = new Subscription();

  constructor(private authorsService: AuthorsService) {
  }

  ngOnInit(): void {
    this.subscribeToAuthorsChanges();
    this.authors = this.authorsService.getAuthors();

    this.subscribeToMessageChanges();
    this.message = this.authorsService.getMessage();
  }

  ngOnDestroy(): void {
    this.authorsSubscription.unsubscribe();
    this.messageSubscription.unsubscribe();
  }

  private subscribeToAuthorsChanges(): void {
    this.authorsSubscription = this.authorsService.authorsChanged.subscribe((authors: Author[]) => {
      this.authors = authors;
    });
  }

  private subscribeToMessageChanges(): void {
    this.messageSubscription = this.authorsService.messageChanged.subscribe((message: string) => {
      this.message = message;
    });
  }
}
