package com.connectify.connectify.Repository;

import com.connectify.connectify.Entity.Rating;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {

    Rating findByUserId(Long userId);

    @Query("SELECT r2 " +
            "FROM Rating r1 " +
            "JOIN Rating r2 " +
            "ON ((ABS(r1.ranking - r2.ranking) BETWEEN 0 AND 40000 " +
            "AND r1.ranking <> 0 AND r2.ranking <> 0) " +
            "OR (ABS(r2.rating - r1.rating) BETWEEN 0 AND 500 " +
            "AND r1.rating <> 0 AND r2.rating <> 0)) " +
            "WHERE r1.user.id <> r2.user.id AND r1.user.id = :currentUserId " +
            "AND r2.user.id NOT IN (SELECT cr.fromUser.id from ConnectionRequest cr " +
            "WHERE cr.toUser = :currentUserId " +
            "UNION SELECT cr.toUser from ConnectionRequest cr where cr.fromUser.id = :currentUserId " +
            "UNION SELECT ac.activeUser.id from ActiveConnection ac where ac.userId = :currentUserId) ")
    List<Rating> findAllRelatableUsers(@Param("currentUserId") Long userId);

    @Query("SELECT r1 from Rating r1 where r1.user.id in " +
            "(SELECT cr.fromUser.id from ConnectionRequest cr where cr.toUser = :currentUserId)")
    List<Rating> findAllPendingRequest(@Param("currentUserId") Long userId);

    @Query("SELECT r1 from Rating r1 where r1.user.id in " +
            "(SELECT ac.activeUser.id from ActiveConnection ac where ac.userId = :userId )")
    List<Rating> findAllActiveUsers(Long userId);
}
