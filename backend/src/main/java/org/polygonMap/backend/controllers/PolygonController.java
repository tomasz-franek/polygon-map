package org.polygonMap.backend.controllers;

import lombok.AllArgsConstructor;
import org.polygonMap.api.PolygonApi;
import org.polygonMap.backend.services.PolygonService;
import org.polygonMap.model.Polygon;
import org.polygonMap.model.SavePolygon201Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;

@Controller
@AllArgsConstructor
public class PolygonController implements PolygonApi {

    private final PolygonService polygonService;

    @Override
    public ResponseEntity<Polygon> getPolygon(String polygonId) {
        return ResponseEntity.ok(polygonService.findByPolygonId(polygonId));
    }

    @Override
    public ResponseEntity<SavePolygon201Response> savePolygon(Polygon polygon) {
        String polygonId = polygonService.savePolygon(polygon);
        return ResponseEntity.created(URI.create(polygonId)).body(new SavePolygon201Response(polygonId));
    }

    @Override
    public ResponseEntity<Void> updatePolygon(String polygonId, Polygon polygon) {
        polygonService.updatePolygon(polygonId, polygon);
        return ResponseEntity.noContent().build();
    }
}
