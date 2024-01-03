import {Component, Input, OnInit} from '@angular/core';
import {AuthorizationService} from '../auth/service/authorization.service';
import {UserData} from '../auth/shared/user-data-type';
import {Router} from '@angular/router';
import {DataStorageService} from '../shared/data-storage.service';
import {Author} from '../authors/dto/author.type';

@Component({
  selector: 'navigation-board',
  templateUrl: './navigation-board.component.html',
  styleUrls: ['./navigation-board.component.scss']
})
export class NavigationBoardComponent implements OnInit {
  @Input() user!: UserData;

  isLoggedIn$ = this.authorizationService.isLoggedIn$;

  constructor(
    private authorizationService: AuthorizationService,
    private dataStorageService: DataStorageService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.authorizationService.getUser().subscribe((user) => {
      this.user = user || new UserData('', '', '', '', '', '', '', '', '', 0, 0);
    });
  }

  logout(): void {
    this.authorizationService.logout();
  }

  refreshData(routeSegment: 'following' | 'followers', user: UserData): void {
    const author = new Author(user.id, user.firstName, user.lastName, user.username, [], []);
    this.router.navigate(['/authors', routeSegment]).then(() => {
      if (routeSegment === 'following') {
        this.dataStorageService.fetchFollowingAuthors(author);
      } else {
        this.dataStorageService.fetchFollowersAuthors(author);
      }
    });
  }
}
