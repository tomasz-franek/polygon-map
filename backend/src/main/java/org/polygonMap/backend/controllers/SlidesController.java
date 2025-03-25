package org.polygonMap.backend.controllers;

import lombok.AllArgsConstructor;
import org.polygonMap.api.SlidesApi;
import org.polygonMap.backend.services.SlidesService;
import org.polygonMap.model.MapSlideShow;
import org.polygonMap.model.SaveMapSlideShow201Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;

@Controller
@AllArgsConstructor
public class SlidesController implements SlidesApi {

    private final SlidesService slidesService;

    @Override
    public ResponseEntity<MapSlideShow> getSlideShow(String mapSlideShowId) {
        return ResponseEntity.ok(slidesService.getSlideShow(mapSlideShowId));
    }

    @Override
    public ResponseEntity<SaveMapSlideShow201Response> saveMapSlideShow(MapSlideShow mapSlideShow) {
        String mapSlideShowId = slidesService.saveMapSlideShow(mapSlideShow);
        return ResponseEntity.created(URI.create(mapSlideShowId)).body(new SaveMapSlideShow201Response(mapSlideShowId));
    }

    @Override
    public ResponseEntity<Void> updateSlideShow(String mapSlideShowId, MapSlideShow mapSlideShow) {
        slidesService.updateSlideShow(mapSlideShowId, mapSlideShow);
        return ResponseEntity.noContent().build();
    }
}
