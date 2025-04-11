import {MapState} from './map.selectors';
import {createReducer, on} from '@ngrx/store';
import {retrievedSlideShowSuccess, setSlideShowId} from './map.action';

export const initialMapState:MapState ={
  slideShowId:'',
  slideShow: undefined
}

export const mapReducer = createReducer(
  initialMapState,
  on(setSlideShowId,(state,action):MapState=>{
    return {...state,slideShowId:action.slideShowId};
  }),
  on(retrievedSlideShowSuccess,(state,action): MapState=>{
    return {...state,slideShow:action.slideShow};
  })
)
