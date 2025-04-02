package org.polygonMap.backend.services;

import lombok.AllArgsConstructor;
import org.polygonMap.backend.repositories.SlideShowRepository;
import org.polygonMap.model.SearchResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SlideShowRepository slideShowRepository;

    @Override
    public List<SearchResult> getSearchResultData(String slideName) {
        return slideShowRepository.getSearchResultData(slideName, Pageable.ofSize(2));
    }
}
