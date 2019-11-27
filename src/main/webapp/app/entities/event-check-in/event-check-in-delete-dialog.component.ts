import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEventCheckIn } from 'app/shared/model/event-check-in.model';
import { EventCheckInService } from './event-check-in.service';

@Component({
  selector: 'jhi-event-check-in-delete-dialog',
  templateUrl: './event-check-in-delete-dialog.component.html'
})
export class EventCheckInDeleteDialogComponent {
  eventCheckIn: IEventCheckIn;

  constructor(
    protected eventCheckInService: EventCheckInService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.eventCheckInService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'eventCheckInListModification',
        content: 'Deleted an eventCheckIn'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-event-check-in-delete-popup',
  template: ''
})
export class EventCheckInDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ eventCheckIn }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EventCheckInDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.eventCheckIn = eventCheckIn;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/event-check-in', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/event-check-in', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
