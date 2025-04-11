import {inject, Injectable} from '@angular/core';
import {ApiService} from '../../services/api.service';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {retrievedSlideShowError, retrievedSlideShowSuccess, retrieveSlideShow} from './map.action';
import {catchError, map, mergeMap} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable()
export class MapEffects {
  private _apiService$: ApiService = inject(ApiService);

  loadSlideShow$ = createEffect(() => {
    return inject(Actions).pipe(
      ofType(retrieveSlideShow),
      mergeMap((action) => {
        return this._apiService$.getSlideShow(action.slideShowId).pipe(
          map((slideShow) => {
            return retrievedSlideShowSuccess({
              slideShow,
            });
          })
        )
      }),
      catchError((error: HttpErrorResponse) => {
        return [retrievedSlideShowError({error})];
      })
    );
  });
}
