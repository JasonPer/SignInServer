/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { EventCheckInService } from 'app/entities/event-check-in/event-check-in.service';
import { IEventCheckIn, EventCheckIn } from 'app/shared/model/event-check-in.model';

describe('Service Tests', () => {
  describe('EventCheckIn Service', () => {
    let injector: TestBed;
    let service: EventCheckInService;
    let httpMock: HttpTestingController;
    let elemDefault: IEventCheckIn;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(EventCheckInService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new EventCheckIn(0, 'AAAAAAA', 0, 'AAAAAAA', currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            checkTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a EventCheckIn', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            checkTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            checkTime: currentDate
          },
          returnedFromService
        );
        service
          .create(new EventCheckIn(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a EventCheckIn', async () => {
        const returnedFromService = Object.assign(
          {
            userName: 'BBBBBB',
            phoneNumber: 1,
            address: 'BBBBBB',
            checkTime: currentDate.format(DATE_TIME_FORMAT),
            isCheckIn: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            checkTime: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of EventCheckIn', async () => {
        const returnedFromService = Object.assign(
          {
            userName: 'BBBBBB',
            phoneNumber: 1,
            address: 'BBBBBB',
            checkTime: currentDate.format(DATE_TIME_FORMAT),
            isCheckIn: true
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            checkTime: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a EventCheckIn', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
