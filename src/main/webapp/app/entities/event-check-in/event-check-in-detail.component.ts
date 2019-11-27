import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventCheckIn } from 'app/shared/model/event-check-in.model';

@Component({
  selector: 'jhi-event-check-in-detail',
  templateUrl: './event-check-in-detail.component.html'
})
export class EventCheckInDetailComponent implements OnInit {
  eventCheckIn: IEventCheckIn;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ eventCheckIn }) => {
      this.eventCheckIn = eventCheckIn;
    });
  }

  previousState() {
    window.history.back();
  }
}
