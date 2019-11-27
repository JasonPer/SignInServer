import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SignInServerSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [SignInServerSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [SignInServerSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SignInServerSharedModule {
  static forRoot() {
    return {
      ngModule: SignInServerSharedModule
    };
  }
}
