package org.polygonMap.backend.services;

import lombok.AllArgsConstructor;
import org.polygonMap.backend.exceptions.NotFoundEntityException;
import org.polygonMap.backend.exceptions.ValidationException;
import org.polygonMap.backend.repositories.SlideShowRepository;
import org.polygonMap.backend.validators.ValidationResult;
import org.polygonMap.model.Polygon;
import org.polygonMap.model.Slide;
import org.polygonMap.model.SlideShow;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SlideServiceImpl implements SlideService {

    private SlideShowRepository slideShowRepository;

    @Override
    @CacheEvict(cacheNames = "slideShows")
    public SlideShow getSlideShow(String slideShowId) {
        SlideShow slideShowEntity = slideShowRepository.findBySlideShowId(slideShowId);
        if (slideShowEntity == null) {
            throw new NotFoundEntityException(SlideShow.class, slideShowId);
        }
        return slideShowEntity;
    }

    @Override
    @CacheEvict(cacheNames = "slideShows", allEntries = true)
    public String saveSlideShow(SlideShow slideShow) {
        slideShow.setSlideShowId(UUID.randomUUID().toString());
        slideShow.setCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return slideShowRepository.insert(slideShow).getSlideShowId();
    }

    @Override
    @CacheEvict(cacheNames = "slideShows", allEntries = true)
    public void updateSlideShow(String slideShowId, SlideShow slideShow) {
        SlideShow slideShowEntity = getSlideShow(slideShowId);
        slideShowEntity.setSlides(slideShow.getSlides());
        slideShowEntity.setCenterPoint(slideShow.getCenterPoint());
        updateLastModifiedDateAndSave(slideShowEntity);
    }


    @Override
    @CacheEvict(cacheNames = "slideShows", allEntries = true)
    public boolean deleteSlideStep(String slideShowId, String slideId) {
        SlideShow slideShowEntity = getSlideShow(slideShowId);
        boolean removed = slideShowEntity.getSlides().removeIf(slide -> slide.getSlideId().equals(slideId));
        if (removed) {
            updateLastModifiedDateAndSave(slideShowEntity);
        }
        return removed;
    }

    @Override
    @CacheEvict(cacheNames = "slideShows", allEntries = true)
    public boolean duplicateSlideStep(String slideShowId, String slideId) {
        SlideShow slideShowEntity = getSlideShow(slideShowId);
        int size = slideShowEntity.getSlides().size();
        for (int i = 0; i < size; i++) {
            if (slideShowEntity.getSlides().get(i).getSlideId().equals(slideId)) {
                Slide currentStep = slideShowEntity.getSlides().get(i);
                Slide newStep = copySlide(currentStep);
                slideShowEntity.getSlides().add(i + 1, newStep);
                updateLastModifiedDateAndSave(slideShowEntity);
                return true;
            }
        }
        return false;
    }

    @Override
    @CacheEvict(cacheNames = "slideShows", allEntries = true)
    public boolean updateSlide(String slideShowId, String slideId, List<Polygon> polygons) {
        if (polygons.isEmpty()) {
            throw new ValidationException("Polygons list can't be empty", new ValidationResult());
        }
        SlideShow slideShowEntity = getSlideShow(slideShowId);
        int size = slideShowEntity.getSlides().size();
        for (int i = 0; i < size; i++) {
            if (slideShowEntity.getSlides().get(i).getSlideId().equals(slideId)) {
                Slide currentStep = slideShowEntity.getSlides().get(i);
                currentStep.getPolygons().clear();
                currentStep.getPolygons().addAll(polygons);
                updateLastModifiedDateAndSave(slideShowEntity);
                return true;
            }
        }
        return false;
    }

    private Slide copySlide(Slide currentStep) {
        return new Slide(UUID.randomUUID().toString(), new ArrayList<>(currentStep.getPolygons()));
    }

    @Override
    @CacheEvict(cacheNames = "slideShows", allEntries = true)
    public String saveSlideStep(String slideShowId, Slide slide) {
        if (slide == null || slide.getPolygons().isEmpty()) {
            throw new ValidationException("Slide can't be empty", new ValidationResult());
        }
        SlideShow slideShowEntity = getSlideShow(slideShowId);

        slideShowEntity.getSlides().add(copySlide(slide));
        return updateLastModifiedDateAndSave(slideShowEntity).getId();
    }

    private SlideShow updateLastModifiedDateAndSave(SlideShow slideShowEntity) {
        slideShowEntity.setLastModifiedDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return slideShowRepository.save(slideShowEntity);
    }
}
