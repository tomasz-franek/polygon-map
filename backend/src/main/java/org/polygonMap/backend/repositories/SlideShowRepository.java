package org.polygonMap.backend.repositories;

import org.polygonMap.model.SearchResult;
import org.polygonMap.model.SlideShow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideShowRepository extends MongoRepository<SlideShow, String> {
    SlideShow findBySlideShowId(String slideShowId);

    @Query(value = "{ 'title' : { $regex: /?0/ , $options: 'i'} }", fields = "{ 'id':0, 'slideShowId':1, 'title':2, 'description':3 }")
    List<SearchResult> getSearchResultData(String title, Pageable pageable);
}
