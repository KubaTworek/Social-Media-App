import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private articleListUpdatedSource = new BehaviorSubject<string>('');
  articleListUpdated$ = this.articleListUpdatedSource.asObservable();

  updateArticleList(keyword: string): void {
    this.articleListUpdatedSource.next(keyword);
  }
}
