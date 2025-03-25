package org.polygonMap.backend.repositories;

import org.polygonMap.model.MapSlideShow;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapSlideShowRepository extends MongoRepository<MapSlideShow, String> {
    MapSlideShow findByMapSlideShowId(String mapSlideShowId);
}
