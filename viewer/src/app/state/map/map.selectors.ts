import {Features} from "../../../features";
import {createFeatureSelector, createSelector} from '@ngrx/store';
import {SlideShow} from '../../api';

export interface MapState {
  slideShowId: string;
  slideShow: SlideShow|undefined;
  slidesCount: number;
  currentSlide: number;
}

export const selectMapFutureState = createFeatureSelector<MapState>(
  Features.maps
)

export const getShowSelector = createSelector(
  selectMapFutureState,
  (state) => state.slideShow
);

export const getCurrentSlide = createSelector(
  selectMapFutureState,
  (state) => state.currentSlide
);

export const getSlidesCount = createSelector(
  selectMapFutureState,
  (state) => state.slidesCount
);
