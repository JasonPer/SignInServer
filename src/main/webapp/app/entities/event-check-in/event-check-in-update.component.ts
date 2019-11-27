import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IEventCheckIn, EventCheckIn } from 'app/shared/model/event-check-in.model';
import { EventCheckInService } from './event-check-in.service';

@Component({
  selector: 'jhi-event-check-in-update',
  templateUrl: './event-check-in-update.component.html'
})
export class EventCheckInUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    userName: [],
    phoneNumber: [],
    address: [],
    checkTime: [],
    isCheckIn: []
  });

  constructor(protected eventCheckInService: EventCheckInService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ eventCheckIn }) => {
      this.updateForm(eventCheckIn);
    });
  }

  updateForm(eventCheckIn: IEventCheckIn) {
    this.editForm.patchValue({
      id: eventCheckIn.id,
      userName: eventCheckIn.userName,
      phoneNumber: eventCheckIn.phoneNumber,
      address: eventCheckIn.address,
      checkTime: eventCheckIn.checkTime != null ? eventCheckIn.checkTime.format(DATE_TIME_FORMAT) : null,
      isCheckIn: eventCheckIn.isCheckIn
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const eventCheckIn = this.createFromForm();
    if (eventCheckIn.id !== undefined) {
      this.subscribeToSaveResponse(this.eventCheckInService.update(eventCheckIn));
    } else {
      this.subscribeToSaveResponse(this.eventCheckInService.create(eventCheckIn));
    }
  }

  private createFromForm(): IEventCheckIn {
    return {
      ...new EventCheckIn(),
      id: this.editForm.get(['id']).value,
      userName: this.editForm.get(['userName']).value,
      phoneNumber: this.editForm.get(['phoneNumber']).value,
      address: this.editForm.get(['address']).value,
      checkTime:
        this.editForm.get(['checkTime']).value != null ? moment(this.editForm.get(['checkTime']).value, DATE_TIME_FORMAT) : undefined,
      isCheckIn: this.editForm.get(['isCheckIn']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventCheckIn>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
