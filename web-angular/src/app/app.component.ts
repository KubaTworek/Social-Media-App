import {Component} from '@angular/core';
import {SearchService} from "./search-board/service/search.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [SearchService]
})
export class AppComponent {
  title = 'web-angular';
  loadedFeature = 'articles';

  onNavigate(feature: string) {
    this.loadedFeature = feature;
  }
}
