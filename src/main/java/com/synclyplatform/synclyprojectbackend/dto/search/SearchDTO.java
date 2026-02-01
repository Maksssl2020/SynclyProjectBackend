package com.synclyplatform.synclyprojectbackend.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {

    private Long id;
    private String searchName;
    private Long searchCount;
    private String searchDate;
}
