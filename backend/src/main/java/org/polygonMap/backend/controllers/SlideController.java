package org.polygonMap.backend.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.polygonMap.api.SlideApi;
import org.polygonMap.backend.services.SlideService;
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
public class SlideController implements SlideApi {

    private final SlideService slideService;

    @Override
    public ResponseEntity<SlideShow> getSlideShow(String slideShowId) {
        return ResponseEntity.ok(slideService.getSlideShow(slideShowId));
    }

    @Override
    public ResponseEntity<SaveSlideShow201Response> saveSlideShow(SlideShow slideShow) {
        String slideShowId = slideService.saveSlideShow(slideShow);
        return ResponseEntity.created(URI.create(slideShowId)).body(new SaveSlideShow201Response(slideShowId));
    }

    @Override
    public ResponseEntity<Void> updateSlideShow(String slideShowId, SlideShow slideShow) {
        slideService.updateSlideShow(slideShowId, slideShow);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteSlideStep(String slideShowId, String slideId) {
        if (slideService.deleteSlideStep(slideShowId, slideId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<SaveSlideStep201Response> saveSlideStep(String slideShowId, Slide slide) {
        String slideStepId = slideService.saveSlideStep(slideShowId, slide);
        return ResponseEntity.created(null).body(new SaveSlideStep201Response(slideStepId));
    }

    @Override
    public ResponseEntity<Void> updateSlide(String slideShowId, String slideId, List<@Valid Polygon> polygons) {
        if (slideService.updateSlide(slideShowId, slideId, polygons)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> duplicateSlideStep(String slideShowId, String slideId) {
        if (slideService.duplicateSlideStep(slideShowId, slideId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
