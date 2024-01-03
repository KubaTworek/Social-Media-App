import {Component, Input, OnInit, ViewEncapsulation} from "@angular/core";
import {Notification} from "../../dto/notification.type";
import {DataStorageService} from "../../../shared/data-storage.service";
import {AuthorDto} from "../../../articles/dto/author.type";
import {AuthorizationService} from "../../../auth/service/authorization.service";

@Component({
  selector: 'notification-card',
  templateUrl: './notification-card.component.html',
  styleUrls: ['./notification-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NotificationCardComponent implements OnInit {
  @Input() notification!: Notification;
  authors: AuthorDto[] = [];
  selectedAuthorId: string = '';
  authorsLoaded: boolean = false;

  constructor(
    private dataStorageService: DataStorageService,
    private authorizationService: AuthorizationService
  ) {
  }

  ngOnInit() {
    this.loadAuthors();
  }

  loadAuthors() {
    if (!this.authorsLoaded && this.isAdmin()) {
      this.dataStorageService.fetchAuthors().subscribe(authors => {
        this.authors = authors;
        this.authorsLoaded = true;
      });
    }
  }

  updateAuthor() {
    const selectedAuthor = this.authors.find(author => author.id == this.selectedAuthorId);
    if (selectedAuthor) {
      this.notification.name = `${selectedAuthor.firstName} ${selectedAuthor.lastName}`;
    }

    this.dataStorageService.updateNotification(this.notification.id, this.selectedAuthorId);
  }

  isAdmin(): boolean {
    const role = this.authorizationService.getRole();
    return role == 'ROLE_ADMIN';
  }
}
