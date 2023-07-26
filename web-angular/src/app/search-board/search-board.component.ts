import {Component, OnInit} from '@angular/core';
import {SearchService} from "./service/search.service";

@Component({
  selector: 'search-board',
  templateUrl: './search-board.component.html',
  styleUrls: ['./search-board.component.scss']
})
export class SearchBoardComponent implements OnInit {

  constructor(private searchService: SearchService) {
  }

  ngOnInit(): void {
  }

  updateArticleList(event: Event): void {
    const inputText = (event.target as HTMLInputElement).value;
    this.searchService.updateArticleList(inputText);
  }
}
