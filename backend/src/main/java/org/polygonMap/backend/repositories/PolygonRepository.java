package org.polygonMap.backend.repositories;

import org.polygonMap.model.Polygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolygonRepository extends MongoRepository<Polygon, String> {
    Polygon findByPolygonId(String polygonId);
}
