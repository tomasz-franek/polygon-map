package org.polygonMap.backend.services;

import lombok.AllArgsConstructor;
import org.polygonMap.backend.exceptions.NotFoundEntityException;
import org.polygonMap.backend.repositories.MapSlideShowRepository;
import org.polygonMap.model.MapSlideShow;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SlidesServiceImpl implements SlidesService {

    private MapSlideShowRepository mapSlideShowRepository;

    @Override
    public MapSlideShow getSlideShow(String mapSlideShowId) {
        return mapSlideShowRepository.findByMapSlideShowId(mapSlideShowId);
    }

    @Override
    public String saveMapSlideShow(MapSlideShow mapSlideShow) {
        mapSlideShow.setMapSlideShowId(UUID.randomUUID().toString());
        return mapSlideShowRepository.insert(mapSlideShow).getMapSlideShowId();
    }

    @Override
    public void updateSlideShow(String mapSlideShowId, MapSlideShow mapSlideShow) {
        MapSlideShow mapSlideShowEntity = mapSlideShowRepository.findByMapSlideShowId(mapSlideShowId);
        if (mapSlideShowEntity == null) {
            throw new NotFoundEntityException(MapSlideShow.class, mapSlideShowId);
        }
        mapSlideShowEntity.setMapSlides(mapSlideShow.getMapSlides());
        mapSlideShowEntity.setCenterPoint(mapSlideShow.getCenterPoint());
        mapSlideShowRepository.save(mapSlideShowEntity);
    }
}
