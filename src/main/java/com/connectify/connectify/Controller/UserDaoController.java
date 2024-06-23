package com.connectify.connectify.Controller;

import com.connectify.connectify.Service.UserService;
import com.connectify.connectify.Entity.UserDao;
import com.connectify.connectify.Service.UserDaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class UserDaoController {
    @Autowired
    private UserDaoService userDaoService;
    @Autowired
    private UserService userService;
    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public UserDao addUser(@Payload UserDao userDao){
        userDaoService.saveUser(userDao);
        return userDao;
    }
    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public UserDao disconnect(@Payload UserDao userDao){
        userDaoService.disconnect(userDao);
        return userDao;
    }
    @GetMapping("/users/{gmailId}")
    public ResponseEntity<List<UserDao>> findConnectedUsers(@PathVariable("gmailId") String gmailId){
        List<Long> userIds = userService.findConnectedUsersId(gmailId);
        List<String> gmailIds = userService.findGmailIdsByIds(userIds);
        Set<String> gidSet = new HashSet<>(gmailIds);
        System.out.println(gidSet);
        List<UserDao> userlist = userDaoService.findConnectedUsers();
        List<UserDao> newUserDaoList = new ArrayList<>();
        for(UserDao ud: userlist){
            if(gidSet.contains(ud.getGmailId())){
                newUserDaoList.add(ud);
            }
        }
        return ResponseEntity.ok(newUserDaoList);
    }
}
