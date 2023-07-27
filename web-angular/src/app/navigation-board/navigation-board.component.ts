import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'navigation-board',
  templateUrl: './navigation-board.component.html',
  styleUrls: ['./navigation-board.component.scss']
})
export class NavigationBoardComponent implements OnInit {
  @Output() featureSelected = new EventEmitter<string>()

  ngOnInit(): void {
  }

  onSelect(feature: string) {
    this.featureSelected.emit(feature)
  }
}
