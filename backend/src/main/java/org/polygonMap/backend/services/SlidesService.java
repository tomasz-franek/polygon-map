package org.polygonMap.backend.services;

import org.polygonMap.model.Polygon;
import org.polygonMap.model.Slide;
import org.polygonMap.model.SlideShow;

import java.util.List;

public interface SlidesService {
    SlideShow getSlideShow(String slideShowId);

    String saveSlideShow(SlideShow slideShow);

    void updateSlideShow(String slideShowId, SlideShow slideShow);

    void deleteSlideStep(String slideShowId, String slideId);

    boolean duplicateSlideStep(String slideShowId, String slideId);

    boolean updateSlide(String slideShowId, String slideId, List<Polygon> polygons);

    String saveSlideStep(String slideShowId, Slide slide);
}
