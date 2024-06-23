package com.connectify.connectify.Service;

import com.connectify.connectify.Entity.UserDao;
import com.connectify.connectify.Model.Status;
import com.connectify.connectify.Repository.UserDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserDaoService {

    @Autowired
    private UserDaoRepository userDaoRepository;

    public void saveUser(UserDao userDao){
        userDao.setStatus(Status.ONLINE);
        userDaoRepository.save(userDao);
    }
    public void disconnect(UserDao userDao){
        UserDao storedUser = userDaoRepository.findById(userDao.getUsername()).orElse(null);
        if(storedUser != null){
            storedUser.setStatus(Status.OFFLINE);
            userDaoRepository.save(userDao);
        }

    }
    public List<UserDao> findConnectedUsers(){
        return userDaoRepository.findAllByStatus(Status.ONLINE);
    }

    public UserDao findByGmailId(String gmailId) {
        return userDaoRepository.findByGmailId(gmailId);
    }
}
