package com.connectify.connectify.Repository;

import com.connectify.connectify.Entity.ConnectionRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest,Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ConnectionRequest cr " +
            "WHERE cr.fromUser.id = :id2 AND cr.toUser = :id1")
    void removeConnectionRequest(Long id1, Long id2);
}
