package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.activity.Activity;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("""
        SELECT a FROM Activity a
        WHERE (:actionType IS NULL OR a.actionType = :actionType)
          AND (:targetType IS NULL OR a.targetType = :targetType)
          AND (
              :searchEnabled = false OR
              LOWER(a.target) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR
              LOWER(a.user.username) LIKE LOWER(CONCAT('%', :searchQuery, '%'))
          )
    """)
    Page<Activity> findAllFiltered(
            @Param("actionType") ActivityActionType actionType,
            @Param("targetType") ActivityTargetType targetType,
            @Param("searchQuery") String searchQuery,
            @Param("searchEnabled") boolean searchEnabled,
            Pageable pageable
    );
}
