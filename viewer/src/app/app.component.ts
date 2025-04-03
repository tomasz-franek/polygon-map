import {Component, OnInit} from '@angular/core';
import {LeafletControlLayersConfig, LeafletModule} from '@bluehalo/ngx-leaflet';
import {LatLng, latLng, polygon, tileLayer} from 'leaflet';
import * as jsonData from '../assets/europe.json';

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
    jsonData.features.forEach((feature:any) => {

      if (feature.geometry.type == 'Polygon') {
        const points: LatLng[] = [];
        feature.geometry.coordinates.forEach((coordinate:any) => {
          coordinate.forEach((item:any) => {
            if(typeof item[0] == 'number' && typeof item[1] == 'number') {
              points.push(new LatLng(item[1],item[0]));
            }
          })
        })
        this.layersControl.overlays[feature.properties.NAME] =  polygon(points);
      }
      if (feature.geometry.type == 'MultiPolygon') {
        const polyLinePoints: any = [];
        feature.geometry.coordinates.forEach((coordinate:any) => {
          coordinate.forEach((item:any) => {
            const points: LatLng[] = [];
            if(item instanceof Array) {
                item.forEach((multiItem:any) => {
                  if (multiItem instanceof Array) {
                    if (typeof multiItem[0] == 'number' && typeof multiItem[1] == 'number') {
                      points.push(new LatLng(multiItem[1], multiItem[0]));
                    }
                  }
                })
              }
            polyLinePoints.push(points);
          })
        })
        this.layersControl.overlays[feature.properties.NAME] =  polygon(polyLinePoints);
      }
    })
  }
  title = 'viewer';
  options = {
    layers: [
      tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 15, attribution: 'OpenStreetMap' })
    ],
    zoom: 5,
    center: latLng(45.801919,19.415062)
  };

}
