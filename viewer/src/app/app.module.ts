import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {bootstrapApplication, BrowserModule} from '@angular/platform-browser';
import {LeafletModule} from '@bluehalo/ngx-leaflet';

@NgModule({
  declarations: [],
  imports: [
    BrowserModule,
    LeafletModule
  ],
  providers: []
})
export class AppModule {
}

bootstrapApplication(AppComponent);
