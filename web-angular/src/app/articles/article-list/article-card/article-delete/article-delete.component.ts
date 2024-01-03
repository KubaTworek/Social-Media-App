import {Component, ElementRef, EventEmitter, Output, Renderer2, ViewChild, ViewEncapsulation} from "@angular/core";

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

  constructor(private renderer: Renderer2) {
  }

  open(): void {
    this.isOpen = true;
    this.toggleModalVisibility(true);
  }

  close(): void {
    this.isOpen = false;
    this.toggleModalVisibility(false);
  }

  confirm(): void {
    this.confirmed.emit();
  }

  cancel(): void {
    this.cancelled.emit();
  }

  private toggleModalVisibility(visible: boolean): void {
    const deleteModal = this.deleteModalRef.nativeElement as HTMLElement;
    const overlayModal = this.overlayModalRef.nativeElement as HTMLElement;

    this.renderer.setStyle(deleteModal, 'visibility', visible ? 'visible' : 'hidden');
    this.renderer.setStyle(overlayModal, 'display', visible ? 'block' : 'none');
  }
}
