package org.polygonMap.backend.controllers;

import lombok.AllArgsConstructor;
import org.polygonMap.api.SearchApi;
import org.polygonMap.backend.services.SearchService;
import org.polygonMap.model.SearchResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class SearchController implements SearchApi {

    private final SearchService searchService;

    @Override
    public ResponseEntity<List<SearchResult>> getSearchResultData(String slideName) {
        return ResponseEntity.ok(searchService.getSearchResultData(slideName));
    }
}
