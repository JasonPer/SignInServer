import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SignInServerSharedModule } from 'app/shared';
import {
  EventCheckInComponent,
  EventCheckInDetailComponent,
  EventCheckInUpdateComponent,
  EventCheckInDeletePopupComponent,
  EventCheckInDeleteDialogComponent,
  eventCheckInRoute,
  eventCheckInPopupRoute
} from './';

const ENTITY_STATES = [...eventCheckInRoute, ...eventCheckInPopupRoute];

@NgModule({
  imports: [SignInServerSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    EventCheckInComponent,
    EventCheckInDetailComponent,
    EventCheckInUpdateComponent,
    EventCheckInDeleteDialogComponent,
    EventCheckInDeletePopupComponent
  ],
  entryComponents: [
    EventCheckInComponent,
    EventCheckInUpdateComponent,
    EventCheckInDeleteDialogComponent,
    EventCheckInDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SignInServerEventCheckInModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
