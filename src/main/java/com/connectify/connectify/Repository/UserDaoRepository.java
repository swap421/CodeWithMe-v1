package com.connectify.connectify.Repository;

import com.connectify.connectify.Entity.UserDao;
import com.connectify.connectify.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDaoRepository extends JpaRepository<UserDao, String> {
    public List<UserDao> findAllByStatus(Status status);

    UserDao findByGmailId(String gmailId);
}
