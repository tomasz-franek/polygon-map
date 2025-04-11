import {ApplicationConfig, isDevMode, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {provideHttpClient, withFetch} from '@angular/common/http';
import {provideEffects} from '@ngrx/effects';
import {provideStore} from '@ngrx/store';
import {mapReducer} from './state/map/map.reducer';
import {appRoutes} from './app.routes';
import {provideStoreDevtools} from '@ngrx/store-devtools';
import {MapEffects} from './state/map/map.effects';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(appRoutes),
    provideHttpClient(withFetch()),
    provideEffects(),
    provideStore({
      maps: mapReducer
    }),
    provideStoreDevtools({
      maxAge: 25, // Retains last 25 states
      logOnly: !isDevMode(), // Restrict extension to log-only mode
      traceLimit: 75, // maximum stack trace frames to be stored (in case trace option was provided as true)
      connectInZone: true, // If set to true, the connection is established within the Angular zone
    }),
    provideEffects(MapEffects)
  ]
};
