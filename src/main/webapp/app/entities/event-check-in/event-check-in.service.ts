import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEventCheckIn } from 'app/shared/model/event-check-in.model';

type EntityResponseType = HttpResponse<IEventCheckIn>;
type EntityArrayResponseType = HttpResponse<IEventCheckIn[]>;

@Injectable({ providedIn: 'root' })
export class EventCheckInService {
  public resourceUrl = SERVER_API_URL + 'api/event-check-ins';

  constructor(protected http: HttpClient) {}

  create(eventCheckIn: IEventCheckIn): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventCheckIn);
    return this.http
      .post<IEventCheckIn>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(eventCheckIn: IEventCheckIn): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventCheckIn);
    return this.http
      .put<IEventCheckIn>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEventCheckIn>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEventCheckIn[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(eventCheckIn: IEventCheckIn): IEventCheckIn {
    const copy: IEventCheckIn = Object.assign({}, eventCheckIn, {
      checkTime: eventCheckIn.checkTime != null && eventCheckIn.checkTime.isValid() ? eventCheckIn.checkTime.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.checkTime = res.body.checkTime != null ? moment(res.body.checkTime) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((eventCheckIn: IEventCheckIn) => {
        eventCheckIn.checkTime = eventCheckIn.checkTime != null ? moment(eventCheckIn.checkTime) : null;
      });
    }
    return res;
  }
}
