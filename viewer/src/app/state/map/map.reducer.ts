import {MapState} from './map.selectors';
import {createReducer, on} from '@ngrx/store';
import {
  nextSlide,
  retrievedSlideShowSuccess,
  selectFirstSlide,
  selectLastSlide,
  setSlidesCount,
  setSlideShowId
} from './map.action';

export const initialMapState:MapState ={
  slideShowId:'',
  slideShow: undefined,
  slidesCount: 0,
  currentSlide: 0,
}

export const mapReducer = createReducer(
  initialMapState,
  on(setSlideShowId,(state,action):MapState=>{
    return {...state,slideShowId:action.slideShowId};
  }),
  on(retrievedSlideShowSuccess,(state,action): MapState=>{
    return {...state,slideShow:action.slideShow};
  }),
  on(nextSlide, (state, action): MapState => {
    if (state.slidesCount > 0)
      if (state.currentSlide < state.slidesCount) {
        return {...state, currentSlide: state.currentSlide + 1};
      } else {
        return {...state, currentSlide: 0};
      } else {
      return {...state};
    }
  }),
  on(setSlidesCount, (state, action) => {
    return {...state, slidesCount: action.slidesCount};
  }),
  on(selectFirstSlide, (state, action) => {
    return {...state, currentSlide: 0};
  }),
  on(selectLastSlide, (state, action) => {
    return {...state, currentSlide: state.slidesCount - 1};
  })
)
