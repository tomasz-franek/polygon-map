import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {LeafletModule} from '@bluehalo/ngx-leaflet';
import * as L from 'leaflet';
import {LatLng, latLng, Polygon, polygon} from 'leaflet';
import {interval, Subscription} from 'rxjs';
import {ColorUtil} from './utils/color.util';
import {SlideShow} from './api';
import {MapUtil} from './utils/map.util';
import {Store} from '@ngrx/store';
import {getShowSelector, MapState} from './state/map/map.selectors';
import {retrieveSlideShow} from './state/map/map.action';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [
    LeafletModule,
    CommonModule,
  ]
})
export class AppComponent implements OnInit, OnDestroy {

  private subscription: Subscription | undefined;
  private _storeMap$: Store<MapState> = inject(Store);

  private map: L.Map | undefined;
  private countryOrder: string[] =
    ['Portugal', 'Spain', 'Andorra', 'France', 'Monaco', 'Belgium', 'Netherlands', 'United Kingdom',
      'Ireland', 'Faroe Islands', 'Iceland',
      'Switzerland', 'Liechtenstein', 'Italy', 'Holy See (Vatican City)', 'San Marino', 'Malta', 'Austria', 'Slovenia', 'Croatia', 'Bosnia and Herzegovina',
      'Luxembourg', 'Germany', 'Denmark', 'Czech Republic', 'Poland', 'Slovakia', 'Belarus', 'Lithuania',
      'Latvia', 'Estonia', 'Finland', 'Sweden', 'Norway', 'Hungary', 'Ukraine', 'Republic of Moldova', 'Montenegro', 'Romania', 'Serbia', 'Bulgaria', 'The former Yugoslav Republic of Macedonia',
      'Albania', 'Greece', 'Cyprus', 'Turkey', 'Georgia', 'Armenia', 'Azerbaijan', 'Russia'];
  protected countryMap: Map<string, Polygon> = new Map<string, Polygon>();
  private polygonCount = -1;
  title = 'viewer';

  ngOnInit(): void {
    this.map = MapUtil.createMap(MapUtil.defaultMapOptions());
    this.readSlideShow("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1");

    this.subscription = interval(100).subscribe(() => {
      this.addNextPolygon();
    });
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }


  readSlideShow(slideShowId: string): void {
    this._storeMap$.dispatch(retrieveSlideShow({slideShowId}));
    this._storeMap$.select(getShowSelector).subscribe((slideShow: SlideShow | undefined) => {
      if (slideShow != undefined) {
        this.addSlideShowToMap(slideShow);
      }
    })
  }

  private addSlideShowToMap(slideShow: SlideShow) {
    if (slideShow.slides !== undefined && slideShow.slides.length > 0) {
      this.map?.setView(latLng(slideShow.centerPoint[0], slideShow.centerPoint[1]), slideShow.mapZoom);
      slideShow.slides[0].polygons.forEach(polygonObject => {
        const points: LatLng[] = [];
        let createdPolygon: Polygon | undefined;
        if (polygonObject.coordinates.length == 1) {
          polygonObject.coordinates[0].forEach((item: number[]) => {
            this.addPoint(points, item);
          })
          createdPolygon = this.createPolygon(points);
        } else {
          const polyLinePoints: L.LatLng[][] = [];
          polygonObject.coordinates.forEach((coordinate: number[][]) => {
            const points: LatLng[] = [];
            coordinate.forEach((item: any[]) => {
              this.addPoint(points, item);
            })
            polyLinePoints.push(points);
          });
          createdPolygon = this.createPolygon(polyLinePoints);
        }
        if (createdPolygon != undefined) {
          this.countryMap.set(polygonObject.polygonId || '', createdPolygon);
        }
      })
    }
  }

  private addPoint(points: LatLng[], item: number[]) {
    if (typeof item[0] == 'number' && typeof item[1] == 'number') {
      points.push(new LatLng(item[0], item[1]));
    }
  }

  private createPolygon(points: L.LatLng[] | L.LatLng[][]): Polygon {
    return polygon(points, {
      color: ColorUtil.randomColor(),
      weight: 1,
      interactive: false,
    });
  }

  addNextPolygon() {
    if (this.countryOrder.length > 0) {
      if (this.polygonCount < this.countryOrder.length) {
        const key = this.countryOrder[this.polygonCount];
        const polygon = this.countryMap.get(this.countryOrder[this.polygonCount]) || undefined;
        this.polygonCount += 1;
        if (polygon != undefined && this.map != undefined) {
          polygon.addTo(this.map);
          this.createTooltip(polygon, this.map, key);
        }
      } else {
        this.endSubscription();
      }
    } else {
      if (this.polygonCount < this.countryMap.size) {
        const key = Array.from(this.countryMap.keys())[this.polygonCount];
        const polygon = this.countryMap.get(key) || undefined;
        this.polygonCount += 1;
        if (polygon != undefined && this.map != undefined) {
          polygon.addTo(this.map);
          this.createTooltip(polygon, this.map, key);
        }
      } else {
        this.endSubscription();
      }
    }
  }

  private createTooltip(polygon: Polygon, map: L.Map, key: string) {
    const centerPoints = polygon.getBounds().getCenter();
    const tooltip = L.tooltip().setLatLng(centerPoints).setContent(key);
    tooltip.options.direction = "top";
    tooltip.addTo(map);
  }

  private endSubscription(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
