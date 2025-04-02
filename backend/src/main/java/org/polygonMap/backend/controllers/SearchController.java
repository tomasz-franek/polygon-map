package org.polygonMap.backend.controllers;

import lombok.AllArgsConstructor;
import org.polygonMap.api.SearchApi;
import org.polygonMap.model.GetSearchResultData200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class SearchController implements SearchApi {

    @Override
    public ResponseEntity<GetSearchResultData200Response> getSearchResultData(String slideName) {
        return SearchApi.super.getSearchResultData(slideName);
    }
}
