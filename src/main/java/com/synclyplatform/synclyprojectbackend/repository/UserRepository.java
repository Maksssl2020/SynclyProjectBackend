package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByStatus(UserStatus status);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("""
        SELECT u FROM User u WHERE
        LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<User> searchUserByQuery(@Param("query") String query);
}
