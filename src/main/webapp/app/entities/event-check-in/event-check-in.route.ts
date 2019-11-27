import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { EventCheckIn } from 'app/shared/model/event-check-in.model';
import { EventCheckInService } from './event-check-in.service';
import { EventCheckInComponent } from './event-check-in.component';
import { EventCheckInDetailComponent } from './event-check-in-detail.component';
import { EventCheckInUpdateComponent } from './event-check-in-update.component';
import { EventCheckInDeletePopupComponent } from './event-check-in-delete-dialog.component';
import { IEventCheckIn } from 'app/shared/model/event-check-in.model';

@Injectable({ providedIn: 'root' })
export class EventCheckInResolve implements Resolve<IEventCheckIn> {
  constructor(private service: EventCheckInService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEventCheckIn> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<EventCheckIn>) => response.ok),
        map((eventCheckIn: HttpResponse<EventCheckIn>) => eventCheckIn.body)
      );
    }
    return of(new EventCheckIn());
  }
}

export const eventCheckInRoute: Routes = [
  {
    path: '',
    component: EventCheckInComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'signInServerApp.eventCheckIn.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EventCheckInDetailComponent,
    resolve: {
      eventCheckIn: EventCheckInResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'signInServerApp.eventCheckIn.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EventCheckInUpdateComponent,
    resolve: {
      eventCheckIn: EventCheckInResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'signInServerApp.eventCheckIn.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EventCheckInUpdateComponent,
    resolve: {
      eventCheckIn: EventCheckInResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'signInServerApp.eventCheckIn.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const eventCheckInPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EventCheckInDeletePopupComponent,
    resolve: {
      eventCheckIn: EventCheckInResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'signInServerApp.eventCheckIn.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
