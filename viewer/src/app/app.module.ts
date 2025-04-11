import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {bootstrapApplication, BrowserModule} from '@angular/platform-browser';
import {LeafletModule} from '@bluehalo/ngx-leaflet';
import {StoreModule} from '@ngrx/store';

@NgModule({
  declarations: [],
  imports: [
    BrowserModule,
    LeafletModule,
    AppComponent,
    StoreModule,
  ],
  providers: []
})
export class AppModule {
}

bootstrapApplication(AppComponent);
