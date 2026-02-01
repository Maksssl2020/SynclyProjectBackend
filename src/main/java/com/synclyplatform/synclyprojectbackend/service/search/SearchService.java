package com.synclyplatform.synclyprojectbackend.service.search;

import com.synclyplatform.synclyprojectbackend.dto.search.SearchDTO;
import com.synclyplatform.synclyprojectbackend.dto.search.SearchRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {

    List<SearchDTO> getTrendingSearches();
    void save(SearchRequestDTO searchRequestDTO);
}
