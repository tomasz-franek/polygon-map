import {Features} from "../../../features";
import {createFeatureSelector, createSelector} from '@ngrx/store';
import {SlideShow} from '../../api';

export interface MapState {
  slideShowId: string;
  slideShow: SlideShow|undefined;
}

export const selectMapFutureState = createFeatureSelector<MapState>(
  Features.maps
)

export const getShowSelector = createSelector(
  selectMapFutureState,
  (state) => state.slideShow
);
