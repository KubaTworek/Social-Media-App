import {Component, OnInit} from '@angular/core';
import {AuthorizationService} from "../auth/service/authorization.service";

@Component({
  selector: 'navigation-board',
  templateUrl: './navigation-board.component.html',
  styleUrls: ['./navigation-board.component.scss']
})
export class NavigationBoardComponent implements OnInit {
  constructor(
    private authorizationService: AuthorizationService
  ) {
  }

  ngOnInit(): void {
  }

  isLoggedOut(): boolean {
    return sessionStorage.getItem('jwt') === null;
  }

  isLoggedIn(): boolean {
    return sessionStorage.getItem('jwt') !== null;
  }

  logout(): void {
    this.authorizationService.logout()
  }
}
