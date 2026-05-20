package com.foodapp.food_delivery.controller;

import com.foodapp.food_delivery.dto.SearchResponse;
import com.foodapp.food_delivery.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam String searchText){
        return new ResponseEntity<>(searchService.search(searchText), HttpStatus.OK);
    }
}
