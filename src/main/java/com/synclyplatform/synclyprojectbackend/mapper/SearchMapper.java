package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.search.SearchDTO;
import com.synclyplatform.synclyprojectbackend.model.search.Search;
import org.springframework.stereotype.Component;

@Component
public class SearchMapper {

    public SearchDTO toDTO(Search search) {
        return SearchDTO.builder()
                .id(search.getId())
                .searchName(search.getSearchName())
                .searchCount(search.getSearchCount())
                .searchDate(search.getSearchDate().toString())
                .build();
    }
}
