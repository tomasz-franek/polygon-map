package org.polygonMap.backend.services;

import org.polygonMap.model.MapSlideShow;

public interface SlidesService {
    MapSlideShow getSlideShow(String mapSlideShowId);

    String saveMapSlideShow(MapSlideShow mapSlideShow);

    void updateSlideShow(String mapSlideShowId, MapSlideShow mapSlideShow);
}
