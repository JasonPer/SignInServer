/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { SignInServerTestModule } from '../../../test.module';
import { EventCheckInUpdateComponent } from 'app/entities/event-check-in/event-check-in-update.component';
import { EventCheckInService } from 'app/entities/event-check-in/event-check-in.service';
import { EventCheckIn } from 'app/shared/model/event-check-in.model';

describe('Component Tests', () => {
  describe('EventCheckIn Management Update Component', () => {
    let comp: EventCheckInUpdateComponent;
    let fixture: ComponentFixture<EventCheckInUpdateComponent>;
    let service: EventCheckInService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SignInServerTestModule],
        declarations: [EventCheckInUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EventCheckInUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventCheckInUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EventCheckInService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EventCheckIn(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new EventCheckIn();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
