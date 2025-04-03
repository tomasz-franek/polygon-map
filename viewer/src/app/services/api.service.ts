import {inject, Injectable} from '@angular/core';
import {Polygon, PolygonService, SavePolygon201Response} from '../api/polygon';
import {SearchResult, SearchService} from '../api/search';
import {SaveSlideShow201Response, SaveSlideStep201Response, Slide, SlideService, SlideShow} from '../api/slide';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private polygonService: PolygonService = inject(PolygonService);
  private searchService: SearchService = inject(SearchService);
  private slideService: SlideService = inject(SlideService);

  constructor() {
  }

  searchSlides(slideName: string): Observable<SearchResult[]> {
    return this.searchService.getSearchResultData(slideName);
  }

  getSlideShow(slideShowId: string): Observable<SlideShow> {
    return this.slideService.getSlideShow(slideShowId);
  }

  saveSlideShow(slideShow: SlideShow): Observable<SaveSlideShow201Response> {
    return this.slideService.saveSlideShow(slideShow);
  }

  updateSlide(slideShowId: string, slideId: string, polygons: Polygon[]): Observable<any> {
    return this.slideService.updateSlide(slideShowId, slideId, polygons);
  }

  updateSlideShow(slideShowId: string, slideShow: SlideShow): Observable<any> {
    return this.slideService.updateSlideShow(slideShowId, slideShow);
  }

  deleteSlideStep(slideShowId: string, slideId: string): Observable<any> {
    return this.slideService.deleteSlideStep(slideShowId, slideId);
  }

  duplicateSlideStep(slideShowId: string, slideId: string): Observable<any> {
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

  updatePolygon(polygonId: string, polygon: Polygon): Observable<any> {
    return this.polygonService.updatePolygon(polygonId, polygon);
  }
}
