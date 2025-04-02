package org.polygonMap.backend.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.polygonMap.api.SlidesApi;
import org.polygonMap.backend.services.SlidesService;
import org.polygonMap.model.Polygon;
import org.polygonMap.model.SaveSlideShow201Response;
import org.polygonMap.model.SaveSlideStep201Response;
import org.polygonMap.model.Slide;
import org.polygonMap.model.SlideShow;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.util.List;

@Controller
@AllArgsConstructor
public class SlidesController implements SlidesApi {

    private final SlidesService slidesService;

    @Override
    public ResponseEntity<SlideShow> getSlideShow(String slideShowId) {
        return ResponseEntity.ok(slidesService.getSlideShow(slideShowId));
    }

    @Override
    public ResponseEntity<SaveSlideShow201Response> saveSlideShow(SlideShow slideShow) {
        String slideShowId = slidesService.saveSlideShow(slideShow);
        return ResponseEntity.created(URI.create(slideShowId)).body(new SaveSlideShow201Response(slideShowId));
    }

    @Override
    public ResponseEntity<Void> updateSlideShow(String slideShowId, SlideShow slideShow) {
        slidesService.updateSlideShow(slideShowId, slideShow);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteSlideStep(String slideShowId, String slideId) {
        slidesService.deleteSlideStep(slideShowId, slideId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SaveSlideStep201Response> saveSlideStep(String slideShowId, Slide slide) {
        String slideStepId = slidesService.saveSlideStep(slideShowId, slide);
        return ResponseEntity.created(null).body(new SaveSlideStep201Response(slideStepId));
    }

    @Override
    public ResponseEntity<Void> updateSlide(String slideShowId, String slideId, List<@Valid Polygon> polygons) {
        slidesService.updateSlide(slideShowId, slideId, polygons);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> duplicateSlideStep(String slideShowId, String slideId) {
        slidesService.duplicateSlideStep(slideShowId, slideId);
        return ResponseEntity.noContent().build();
    }
}
