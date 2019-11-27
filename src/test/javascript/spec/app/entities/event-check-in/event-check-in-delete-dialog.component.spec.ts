/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SignInServerTestModule } from '../../../test.module';
import { EventCheckInDeleteDialogComponent } from 'app/entities/event-check-in/event-check-in-delete-dialog.component';
import { EventCheckInService } from 'app/entities/event-check-in/event-check-in.service';

describe('Component Tests', () => {
  describe('EventCheckIn Management Delete Component', () => {
    let comp: EventCheckInDeleteDialogComponent;
    let fixture: ComponentFixture<EventCheckInDeleteDialogComponent>;
    let service: EventCheckInService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SignInServerTestModule],
        declarations: [EventCheckInDeleteDialogComponent]
      })
        .overrideTemplate(EventCheckInDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventCheckInDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EventCheckInService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
