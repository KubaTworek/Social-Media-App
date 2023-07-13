import {Component, ElementRef, EventEmitter, Output, ViewChild, ViewEncapsulation} from "@angular/core";

@Component({
  selector: 'article-delete',
  templateUrl: './article-delete.component.html',
  styleUrls: ['./article-delete.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArticleDeleteComponent {
  @ViewChild('modal') deleteModalRef!: ElementRef;
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  isOpen: boolean = false;

  openModal(): void {
    this.isOpen = true;
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    deleteModal.style.visibility = 'visible';
  }

  closeModal(): void {
    this.isOpen = false;
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    deleteModal.style.visibility = 'hidden';
  }

  confirm(): void {
    this.confirmed.emit();
  }

  cancel(): void {
    this.cancelled.emit();
  }
}
