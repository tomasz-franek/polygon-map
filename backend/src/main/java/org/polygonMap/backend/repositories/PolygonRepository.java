package org.polygonMap.backend.repositories;

import org.polygonMap.model.Polygon;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PolygonRepository extends MongoRepository<Polygon, String> {
    Polygon findByPolygonId(String polygonId);
}
