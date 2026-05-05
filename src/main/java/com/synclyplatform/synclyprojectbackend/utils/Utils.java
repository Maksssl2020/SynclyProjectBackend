package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import org.springframework.data.domain.Sort;

public class Utils {

    public static Sort getTimestampSortOption(TimestampSortOption timestampSortOption, String fieldName) {
        return switch (timestampSortOption) {
            case RECENT -> Sort.by(Sort.Direction.DESC, fieldName);
            case OLDEST -> Sort.by(Sort.Direction.ASC, fieldName);
        };
    }
}
