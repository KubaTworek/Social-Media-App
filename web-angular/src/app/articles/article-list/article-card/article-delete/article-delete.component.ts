import {Component, ElementRef, EventEmitter, Output, ViewChild, ViewEncapsulation} from "@angular/core";

@Component({
  selector: 'article-delete',
  templateUrl: './article-delete.component.html',
  styleUrls: ['./article-delete.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArticleDeleteComponent {
  @ViewChild('modal') deleteModalRef!: ElementRef;
  @ViewChild('overlay') overlayModalRef!: ElementRef;
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  isOpen: boolean = false;

  open(): void {
    this.isOpen = true;
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    const overlayModal = this.overlayModalRef.nativeElement as HTMLElement;
    deleteModal.style.visibility = 'visible';
    overlayModal.style.display = 'block';
  }

  close(): void {
    this.isOpen = false;
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    const overlayModal = this.overlayModalRef.nativeElement as HTMLElement;
    deleteModal.style.visibility = 'hidden';
    overlayModal.style.display = 'none';
  }

  confirm(): void {
    this.confirmed.emit();
  }

  cancel(): void {
    this.cancelled.emit();
  }
}
