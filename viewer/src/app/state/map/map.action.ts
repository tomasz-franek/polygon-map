import {createAction, props} from '@ngrx/store';
import {HttpErrorResponse} from '@angular/common/http';
import {SlideShow} from '../../api';

export const retrieveSlideShow= createAction(
  '[Map] Retrieve SlideShow',
  props<{slideShowId: string}>(),
);

export const retrievedSlideShowSuccess = createAction(
  '[Map] Retrieved Slide Show Success',props<{ slideShow: SlideShow }>()
);

export const nextSlide = createAction(
  '[Map] Next',
);

export const setSlidesCount = createAction(
  '[Map] Set Slides Count', props<{ slidesCount: number }>()
)

export const retrievedSlideShowError= createAction(
  '[Map] Retrieved Slide Show Error',
  props<{
    error: HttpErrorResponse;
  }>()
)

export const setSlideShowId = createAction(
  '[Map] Set Slide Show Id',
  props<{slideShowId: string}>());


export const selectFirstSlide = createAction(
  '[Map] Select First Slide'
)

export const selectLastSlide = createAction(
  '[Map] Select Last Slide'
)
