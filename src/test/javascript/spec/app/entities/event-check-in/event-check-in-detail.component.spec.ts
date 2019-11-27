/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SignInServerTestModule } from '../../../test.module';
import { EventCheckInDetailComponent } from 'app/entities/event-check-in/event-check-in-detail.component';
import { EventCheckIn } from 'app/shared/model/event-check-in.model';

describe('Component Tests', () => {
  describe('EventCheckIn Management Detail Component', () => {
    let comp: EventCheckInDetailComponent;
    let fixture: ComponentFixture<EventCheckInDetailComponent>;
    const route = ({ data: of({ eventCheckIn: new EventCheckIn(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SignInServerTestModule],
        declarations: [EventCheckInDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EventCheckInDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventCheckInDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.eventCheckIn).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
