import * as L from 'leaflet';
import {Map, MapOptions, tileLayer} from 'leaflet';

export class MapUtil{
  public static defaultMapOptions(): MapOptions  {
    return {
      layers: []
    };
  }
  public static createMap(mapOptions:MapOptions):Map{
    const map :Map = L.map('map', mapOptions);
    map.addLayer(tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 15,
      attribution: 'OpenStreetMap'
    }));
    return map;
  }
}
