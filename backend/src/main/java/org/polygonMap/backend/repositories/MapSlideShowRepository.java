package org.polygonMap.backend.repositories;

import org.polygonMap.model.MapSlideShow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapSlideShowRepository extends MongoRepository<MapSlideShow, String> {
    MapSlideShow findByMapSlideShowId(String mapSlideShowId);
}
