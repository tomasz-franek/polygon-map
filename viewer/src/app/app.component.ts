import {Component, OnInit} from '@angular/core';
import {LeafletControlLayersConfig, LeafletModule} from '@bluehalo/ngx-leaflet';
import {LatLng, latLng, Polygon, polygon, tileLayer} from 'leaflet';
import * as jsonData from '../assets/europe.json';
import {v4 as uuid} from 'uuid';
import {Slide} from './api/slide';
import {ColorUtil} from './utils/color.util';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [
    LeafletModule
  ]
})
export class AppComponent implements OnInit {
  public layersControl: LeafletControlLayersConfig = {
    baseLayers: {
      'Open Street Map': tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 18, minZoom:5, attribution: 'OpenStreetMap' })
    },
    overlays: {}
  }

  ngOnInit(): void {
    const slide: Slide = this.parseJsonData();
    slide.polygons.forEach(polygonObject => {
      const points: LatLng[] = [];
      if (polygonObject.coordinates[0].length == 2) {
        polygonObject.coordinates.forEach((item: number[]) => {
          if (typeof item[0] == 'number' && typeof item[1] == 'number') {
            points.push(new LatLng(item[0], item[1]));
          }
        })
        this.layersControl.overlays[polygonObject.polygonId || ''] = this.createPolygon(points, polygonObject.polygonId || '');
      } else {
        const polyLinePoints: any = [];
        polygonObject.coordinates.forEach((coordinate: any[]) => {
          const points: LatLng[] = [];
          coordinate.forEach((item: any[]) => {
            points.push(new LatLng(item[0], item[1]));
          })
          polyLinePoints.push(points);
        })
        this.layersControl.overlays[polygonObject.polygonId || ''] = this.createPolygon(polyLinePoints, polygonObject.polygonId || '');
      }
    })
  }
  title = 'viewer';
  options = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 15, attribution: 'OpenStreetMap' })
    ],
    hideSingleBase: true,
    zoom: 5,
    center: latLng(51.801919, 19.415062)
  };

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

  private createPolygon(points: LatLng[], popupText: string): Polygon {
    return polygon(points, {
      color: ColorUtil.randomColor(),
      weight: 1,
      interactive: false,
    });
  }
}
