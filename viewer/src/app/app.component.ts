import {Component, OnDestroy, OnInit} from '@angular/core';
import {LeafletModule} from '@bluehalo/ngx-leaflet';
import * as L from 'leaflet';
import {LatLng, latLng, Polygon, polygon, tileLayer} from 'leaflet';
import jsonData from '../assets/world_800.json';
import {v4 as uuid} from 'uuid';
import {Slide} from './api/slide';
import {interval, Subscription} from 'rxjs';
import {ColorUtil} from './utils/color.util';

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

  private map: L.Map | undefined;
  private countryOrder: string[] = [];
  // ['Portugal', 'Spain', 'Andorra', 'France', 'Monaco', 'Belgium', 'Netherlands', 'United Kingdom',
  // 'Ireland', 'Faroe Islands', 'Iceland',
  // 'Switzerland', 'Liechtenstein', 'Italy', 'Holy See (Vatican City)', 'San Marino', 'Malta', 'Austria', 'Slovenia', 'Croatia', 'Bosnia and Herzegovina',
  // 'Luxembourg', 'Germany', 'Denmark', 'Czech Republic', 'Poland', 'Slovakia', 'Belarus', 'Lithuania',
  // 'Latvia', 'Estonia', 'Finland', 'Sweden', 'Norway', 'Hungary', 'Ukraine', 'Republic of Moldova', 'Montenegro', 'Romania', 'Serbia', 'Bulgaria', 'The former Yugoslav Republic of Macedonia',
  // 'Albania', 'Greece', 'Cyprus', 'Turkey', 'Georgia', 'Armenia', 'Azerbaijan', 'Russia'];
  protected countryMap: Map<string, Polygon> = new Map<string, Polygon>();
  private polygonCount: number = 0;
  title = 'viewer';
  options = {
    layers: [],
    hideSingleBase: true,
    zoom: 4,
    center: latLng(51.801919, 19.415062)
  };

  ngOnInit(): void {
    this.map = L.map('map', this.options);
    this.map.addLayer(tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 15,
      attribution: 'OpenStreetMap'
    }))
    const slide: Slide = this.parseJsonData();
    slide.polygons.forEach(polygonObject => {
      const points: LatLng[] = [];
      if (polygonObject.coordinates[0].length == 2) {
        polygonObject.coordinates.forEach((item: number[]) => {
          if (typeof item[0] == 'number' && typeof item[1] == 'number') {
            points.push(new LatLng(item[0], item[1]));
          }
        })
        let createdPolygon = this.createPolygon(points);
        this.countryMap.set(polygonObject.polygonId || '', createdPolygon);
      } else {
        const polyLinePoints: any = [];
        polygonObject.coordinates.forEach((coordinate: any[]) => {
          const points: LatLng[] = [];
          coordinate.forEach((item: any[]) => {
            points.push(new LatLng(item[0], item[1]));
          })
          polyLinePoints.push(points);
        })
        let createdPolygon = this.createPolygon(polyLinePoints);
        this.countryMap.set(polygonObject.polygonId || '', createdPolygon);
      }
    })
    this.subscription = interval(100).subscribe(() => {
      this.addNextPolygon();
    });
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }


  parseJsonData(): Slide {
    let slide: Slide = {
      slideId: uuid(),
      polygons: []
    };
    jsonData.features.forEach((feature: any) => {

      if (feature.geometry.type == 'Polygon') {
        const points: Array<Array<number>> = [];
        feature.geometry.coordinates.forEach((coordinate: any) => {
          coordinate.forEach((item: any) => {
            points.push([item[1], item[0]]);
          })
        })
        slide.polygons.push({coordinates: points, polygonId: feature.properties.NAME, id: uuid()});
      }
      if (feature.geometry.type == 'MultiPolygon') {
        const polyLinePoints: any = [];
        feature.geometry.coordinates.forEach((coordinate: any) => {
          coordinate.forEach((item: any) => {
            const points: Array<Array<number>> = [];
            if (item instanceof Array) {
              item.forEach((multiItem: any) => {
                if (multiItem instanceof Array) {
                  if (typeof multiItem[0] == 'number' && typeof multiItem[1] == 'number') {
                    points.push([multiItem[1], multiItem[0]]);
                  }
                }
              })
            }
            polyLinePoints.push(points);
          })
        })
        slide.polygons.push({coordinates: polyLinePoints, polygonId: feature.properties.NAME, id: uuid()});
      }
    })
    return slide;
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
        let polygon = this.countryMap.get(this.countryOrder[this.polygonCount]) || undefined;
        this.polygonCount += 1;
        if (polygon != undefined && this.map != undefined) {
          polygon.addTo(this.map);
        }
      } else {
        if (this.subscription) {
          this.subscription.unsubscribe();
        }
        // this.polygonCount = 0;
        // this.countryMap.forEach((key, _) => {
        //   if (this.map != undefined) {
        //     key.removeFrom(this.map);
        //   }
        // });
      }
    } else {
      if (this.polygonCount < this.countryMap.size) {
        let key = Array.from(this.countryMap.keys())[this.polygonCount];
        console.log(key);
        let polygon = this.countryMap.get(key) || undefined;
        this.polygonCount += 1;
        if (polygon != undefined && this.map != undefined) {
          polygon.addTo(this.map);
          var tooltip = L.tooltip()
            .setLatLng(polygon.getCenter())
            .setContent(key)
            .addTo(this.map);
        }
      } else {
        if (this.subscription) {
          this.subscription.unsubscribe();
        }
        // this.polygonCount = 0;
        // this.countryMap.forEach((key, _) => {
        //   if (this.map != undefined) {
        //     key.removeFrom(this.map);
        //   }
        // });
      }
    }
  }
}
