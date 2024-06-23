package com.connectify.connectify.Repository;

import com.connectify.connectify.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getBygmailId(String gmailId);

    @Query("Select ac.activeUser.id from ActiveConnection ac " +
            "Where ac.userId in (SELECT u.id FROM User u WHERE u.gmailId = :gmailId) ")
    List<Long> findConnectedUsersId(String gmailId);

    @Query("Select u.gmailId from User u " +
            "Where u.id in :userIds ")
    List<String> findGmailIdsByIds(List<Long> userIds);
    @Query("Select u.email from User u " +
            "Where u.gmailId = :gmailId ")
    String findUsernameByGmailId(String gmailId);

}
