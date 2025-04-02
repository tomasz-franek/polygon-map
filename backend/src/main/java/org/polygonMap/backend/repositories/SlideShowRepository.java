package org.polygonMap.backend.repositories;

import org.polygonMap.model.SlideShow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideShowRepository extends MongoRepository<SlideShow, String> {
    SlideShow findBySlideShowId(String slideShowId);
}
