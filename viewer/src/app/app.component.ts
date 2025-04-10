import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {LeafletModule} from '@bluehalo/ngx-leaflet';
import * as L from 'leaflet';
import {LatLng, latLng, Polygon, polygon} from 'leaflet';
import {interval, Subscription} from 'rxjs';
import {ColorUtil} from './utils/color.util';
import {ApiService} from './services/api.service';
import {SlideShow} from './api';
import {MapUtil} from './utils/map.util';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [
    LeafletModule
  ]
})
export class AppComponent implements OnInit, OnDestroy {

  private subscription: Subscription | undefined;
  private apiService: ApiService = inject(ApiService);

  private map: L.Map | undefined;
  private countryOrder: string[] =
    ['Portugal', 'Spain', 'Andorra', 'France', 'Monaco', 'Belgium', 'Netherlands', 'United Kingdom',
      'Ireland', 'Faroe Islands', 'Iceland',
      'Switzerland', 'Liechtenstein', 'Italy', 'Holy See (Vatican City)', 'San Marino', 'Malta', 'Austria', 'Slovenia', 'Croatia', 'Bosnia and Herzegovina',
      'Luxembourg', 'Germany', 'Denmark', 'Czech Republic', 'Poland', 'Slovakia', 'Belarus', 'Lithuania',
      'Latvia', 'Estonia', 'Finland', 'Sweden', 'Norway', 'Hungary', 'Ukraine', 'Republic of Moldova', 'Montenegro', 'Romania', 'Serbia', 'Bulgaria', 'The former Yugoslav Republic of Macedonia',
      'Albania', 'Greece', 'Cyprus', 'Turkey', 'Georgia', 'Armenia', 'Azerbaijan', 'Russia'];
  protected countryMap: Map<string, Polygon> = new Map<string, Polygon>();
  private polygonCount: number = -1;
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
    this.apiService.getSlideShow(slideShowId).subscribe((slideShow: SlideShow) => {
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
            const polyLinePoints: any = [];
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
    })
  }

  private addPoint(points: any[], item: any[]) {
    if (typeof item[0] == 'number' && typeof item[1] == 'number') {
      points.push(new LatLng(item[0], item[1]));
    }
  }

  private createPolygon(points: LatLng[]): Polygon {
    return polygon(points, {
      color: ColorUtil.randomColor(),
      weight: 1,
      interactive: false,
    });
  }

  addNextPolygon() {
    if (this.countryOrder.length > 0) {
      if (this.polygonCount < this.countryOrder.length) {
        let key = this.countryOrder[this.polygonCount];
        let polygon = this.countryMap.get(this.countryOrder[this.polygonCount]) || undefined;
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
        let key = Array.from(this.countryMap.keys())[this.polygonCount];
        let polygon = this.countryMap.get(key) || undefined;
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
    let centerPoints = polygon.getBounds().getCenter();
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
