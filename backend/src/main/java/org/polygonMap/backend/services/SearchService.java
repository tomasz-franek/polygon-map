package org.polygonMap.backend.services;

import org.polygonMap.model.SearchResult;

import java.util.List;

public interface SearchService {
    List<SearchResult> getSearchResultData(String slideName);
}
