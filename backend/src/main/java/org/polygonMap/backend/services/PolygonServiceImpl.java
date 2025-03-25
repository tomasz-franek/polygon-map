package org.polygonMap.backend.services;

import lombok.AllArgsConstructor;
import org.polygonMap.backend.exceptions.NotFoundEntityException;
import org.polygonMap.backend.repositories.PolygonRepository;
import org.polygonMap.model.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PolygonServiceImpl implements PolygonService {
    private final PolygonRepository polygonRepository;

    @Override
    public Polygon findByPolygonId(String polygonId) {
        return polygonRepository.findByPolygonId(polygonId);
    }

    @Override
    public String savePolygon(Polygon polygon) {
        polygon.setPolygonId(UUID.randomUUID().toString());
        return polygonRepository.insert(polygon).getPolygonId();
    }

    @Override
    @Transactional
    public void updatePolygon(String polygonId, Polygon polygon) {
        Polygon polygonEntity = polygonRepository.findByPolygonId(polygonId);
        if (polygonEntity == null) {
            throw new NotFoundEntityException(Polygon.class, polygonId);
        }
        polygonEntity.setPoints(polygon.getPoints());
        polygonEntity.setColor(polygon.getColor());
        polygonRepository.save(polygonEntity);
    }


}
