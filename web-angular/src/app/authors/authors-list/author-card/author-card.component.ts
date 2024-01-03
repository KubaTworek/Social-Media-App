import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {Author} from '../../dto/author.type';
import {DataStorageService} from '../../../shared/data-storage.service';
import {Router} from '@angular/router';

@Component({
  selector: 'author-card',
  templateUrl: './author-card.component.html',
  styleUrls: ['./author-card.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class AuthorCardComponent implements OnInit {
  @Input() author!: Author;

  constructor(
    private dataStorageService: DataStorageService,
    private router: Router
  ) {
  }

  ngOnInit() {
  }

  refreshData(routeSegment: 'following' | 'followers'): void {
    const routePath = `/authors/${routeSegment}`;
    this.router.navigate([routePath]).then(() => {
      if (routeSegment === 'following') {
        this.dataStorageService.fetchFollowingAuthors(this.author);
      } else {
        this.dataStorageService.fetchFollowersAuthors(this.author);
      }
    });
  }
}
