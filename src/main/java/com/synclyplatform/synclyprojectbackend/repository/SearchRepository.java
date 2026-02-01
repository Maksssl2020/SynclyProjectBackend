package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.search.Search;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {

    @Query("""
        SELECT s FROM Search s
        WHERE s.searchDate BETWEEN :today AND :weekAgo
        ORDER BY s.searchCount DESC
    """)
    List<Search> getTrendingSearches(
            @Param("today") LocalDate today,
            @Param("weekAgo") LocalDate weekAgo,
            Pageable pageable
    );

    @Query("""
        SELECT s FROM Search s
        ORDER BY s.searchCount DESC
    """)
    List<Search> findPopularOrderBySearchCountDesc(Pageable pageable);

    @Query("""
    SELECT s FROM Search s
    WHERE s.searchName = :name
      AND s.searchDate BETWEEN :from AND :to
""")
    Optional<Search> findBySearchNameAndDateBetween(
            @Param("name") String name,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );}
