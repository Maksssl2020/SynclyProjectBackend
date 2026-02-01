package com.synclyplatform.synclyprojectbackend.service.search;

import com.synclyplatform.synclyprojectbackend.dto.search.SearchDTO;
import com.synclyplatform.synclyprojectbackend.dto.search.SearchRequestDTO;
import com.synclyplatform.synclyprojectbackend.mapper.SearchMapper;
import com.synclyplatform.synclyprojectbackend.model.search.Search;
import com.synclyplatform.synclyprojectbackend.repository.SearchRepository;
import com.synclyplatform.synclyprojectbackend.repository.TagRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Override
    public List<SearchDTO> getTrendingSearches() {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);

        List<Search> trendingSearches = searchRepository.getTrendingSearches(today, weekAgo, PageRequest.of(0, 8));

        if (trendingSearches.size() < 8) {
            List<Search> popularSearches = searchRepository.findPopularOrderBySearchCountDesc(PageRequest.of(0, 8));

            if (popularSearches.size() < 8) {
                return createRandomPopularSearches(popularSearches);
            }

            return popularSearches.stream()
                    .map(searchMapper::toDTO)
                    .collect(Collectors.toList());
        }

        return trendingSearches.stream()
                .map(searchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void save(SearchRequestDTO searchRequestDTO) {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        String input = searchRequestDTO.getSearchName();

        if (input.isBlank()) {
            return;
        }

        List<String> tagNames = tagRepository.findOnlyTagNames();
        List<String> usernames = userRepository.findOnlyUsersUsernames();
        List<String> displayNames = userRepository.findOnlyUsersDisplayNames();

        List<String> candidates = new ArrayList<>(tagNames);
        candidates.addAll(usernames);
        candidates.addAll(displayNames);


        String finalSearchName = findBestMatch(input, candidates);
        Optional<Search> foundSearch = searchRepository.findBySearchNameAndDateBetween(finalSearchName, weekAgo, today);
        Search search;

        if (foundSearch.isPresent()) {
            search = foundSearch.get();
            search.setSearchCount(search.getSearchCount() + 1);
        } else {
            search = Search.builder()
                    .searchName(finalSearchName)
                    .searchCount(1L)
                    .searchDate(today)
                    .build();
        }

        searchRepository.save(search);
    }

    private String findBestMatch(String input, List<String> candidates) {
        if (candidates.contains(input)) {
            return input;
        }

        // 1. Dokładne dopasowanie (case insensitive)
        for (String candidate : candidates) {
            if (candidate.equalsIgnoreCase(input)) {
                return candidate;
            }
        }

        // 2. Zawieranie
        for (String candidate : candidates) {
            if (candidate.toLowerCase().contains(input.toLowerCase()) ||
                    input.toLowerCase().contains(candidate.toLowerCase())) {
                return candidate;
            }
        }

        // 3. Dopasowanie fuzzy z rozsądnym progiem
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        String bestMatch = input;
        int minDistance = Integer.MAX_VALUE;

        for (String candidate : candidates) {
            int distance = levenshtein.apply(input.toLowerCase(), candidate.toLowerCase());
            if (distance < minDistance && distance <= Math.min(5, input.length() / 2 + 1)) {
                minDistance = distance;
                bestMatch = candidate;
            }
        }

        return minDistance <= 3 ? bestMatch : input;
    }

    private List<SearchDTO> createRandomPopularSearches(List<Search> fetchedPopularSearches) {
        Set<SearchDTO> popularSearches = fetchedPopularSearches.stream().map(searchMapper::toDTO).collect(Collectors.toSet());
        List<String> tagNames = tagRepository.findOnlyTagNames();
        List<String> usernames = userRepository.findOnlyUsersUsernames();
        List<String> displayNames = userRepository.findOnlyUsersDisplayNames();
        Random random = new Random();

        List<String> candidates = new ArrayList<>(tagNames);
        candidates.addAll(usernames);
        candidates.addAll(displayNames);

        int safetyCounter = 0;
        while (popularSearches.size() < 8 && safetyCounter < 100) {
            int randomIndex = random.nextInt(candidates.size());
            String name = candidates.get(randomIndex);

            SearchDTO searchDTO = SearchDTO.builder()
                    .searchName(name)
                    .searchDate(LocalDate.now().toString())
                    .searchCount((long) randomIndex)
                    .build();

            popularSearches.add(searchDTO);
            safetyCounter++;
        }

        return popularSearches.stream().limit(8).toList();
    }
}
