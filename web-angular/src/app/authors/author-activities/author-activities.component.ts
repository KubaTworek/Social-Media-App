import {Component, OnInit} from '@angular/core';
import {AuthorWithActivities} from "../dto/author-with-activities.type";
import {Subscription} from "rxjs";
import {AuthorsService} from "../service/authors.service";

@Component({
  selector: 'author-activities',
  templateUrl: './author-activities.component.html',
  styleUrls: ['./author-activities.component.scss']
})
export class AuthorActivitiesComponent implements OnInit {

  author!: AuthorWithActivities;
  private authorSubscription: Subscription = new Subscription();

  constructor(
    private authorsService: AuthorsService
  ) {
  }

  ngOnInit(): void {
    this.authorSubscription = this.authorsService.activityChanged
      .subscribe(
        (author: AuthorWithActivities) => {
          this.author = author;
        }
      );
    this.author = this.authorsService.getAuthorWithActivities();
  }
}
