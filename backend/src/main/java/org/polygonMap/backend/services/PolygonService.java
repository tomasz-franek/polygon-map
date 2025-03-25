package org.polygonMap.backend.services;

import org.polygonMap.model.Polygon;

public interface PolygonService {
    Polygon findByPolygonId(String polygonId);

    String savePolygon(Polygon polygon);

    void updatePolygon(String polygonId, Polygon polygon);
}
