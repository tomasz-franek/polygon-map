import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {
  Polygon,
  PolygonsService,
  SavePolygon201Response,
  SaveSlideShow201Response,
  SaveSlideStep201Response,
  SearchResult,
  SearchService,
  Slide,
  SlideShow,
  SlidesService
} from '../api';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private polygonService: PolygonsService = inject(PolygonsService);
  private searchService: SearchService = inject(SearchService);
  private slideService: SlidesService = inject(SlidesService);

  searchSlides(slideName: string): Observable<SearchResult[]> {
    return this.searchService.getSearchResultData(slideName);
  }

  getSlideShow(slideShowId: string): Observable<SlideShow> {
    return this.slideService.getSlideShow(slideShowId);
  }

  saveSlideShow(slideShow: SlideShow): Observable<SaveSlideShow201Response> {
    return this.slideService.saveSlideShow(slideShow);
  }

  updateSlide(slideShowId: string, slideId: string, polygons: Polygon[]): Observable<void> {
    return this.slideService.updateSlide(slideShowId, slideId, polygons);
  }

  updateSlideShow(slideShowId: string, slideShow: SlideShow): Observable<void> {
    return this.slideService.updateSlideShow(slideShowId, slideShow);
  }

  deleteSlideStep(slideShowId: string, slideId: string): Observable<void> {
    return this.slideService.deleteSlideStep(slideShowId, slideId);
  }

  duplicateSlideStep(slideShowId: string, slideId: string): Observable<void> {
    return this.slideService.duplicateSlideStep(slideShowId, slideId);
  }

  saveSlideStep(slideShowId: string, slide: Slide): Observable<SaveSlideStep201Response> {
    return this.slideService.saveSlideStep(slideShowId, slide);
  }

  getPolygon(polygonId: string): Observable<Polygon> {
    return this.polygonService.getPolygon(polygonId);
  }

  savePolygon(polygon: Polygon): Observable<SavePolygon201Response> {
    return this.polygonService.savePolygon(polygon);
  }

  updatePolygon(polygonId: string, polygon: Polygon): Observable<void> {
    return this.polygonService.updatePolygon(polygonId, polygon);
  }
}
