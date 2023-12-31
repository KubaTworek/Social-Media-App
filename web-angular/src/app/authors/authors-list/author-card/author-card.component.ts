import {Component, Input, OnInit, ViewEncapsulation} from "@angular/core";
import {Author} from "../../dto/author.type";
import {DataStorageService} from "../../../shared/data-storage.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'author-card',
  templateUrl: './author-card.component.html',
  styleUrls: ['./author-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AuthorCardComponent implements OnInit {
  @Input() author!: Author;

  constructor(
    private dataStorageService: DataStorageService,
    private route: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit() {
  }

  refreshData(routeSegment: 'following' | 'followers', author: Author): void {
    this.router.navigate(['/authors', routeSegment]).then(() => {
      if (routeSegment === 'following') {
        this.dataStorageService.fetchFollowingAuthors(author)
      } else {
        this.dataStorageService.fetchFollowersAuthors(author)
      }
    });
  }
}
