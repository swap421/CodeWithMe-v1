package com.connectify.connectify.Service;

import com.connectify.connectify.Entity.User;
import com.connectify.connectify.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    public ResponseEntity<User> saveUserToDB(User newUser){
        User user = userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    public User getLoggedInUser(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) throws JsonProcessingException {
        if(authorizedClient != null){
            JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
            if(jsonNode != null) {
                String gmailId = jsonNode.get("sub").asText();
                Optional<User> user = getUserByGoogleUniqueId(gmailId);
                if(user.isPresent()){
                    return user.get();
                }
            }
            return null;
        }
        return null;
    }
    public Optional<User> getUserByGoogleUniqueId(String gmailId){
        return userRepository.getBygmailId(gmailId);
    }

    public boolean ratingNotFilled(User user){
        if(user.getLeetcodeUsername() == null && user.getCodeforcesUsername() == null){
            return true;
        }
        else{
            return false;
        }
    }

    public User createNewUser(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                              String leetcodeUsername, String codeforcesUsername) throws JsonProcessingException {
        JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
        String gmailId = jsonNode.get("sub").asText();
        String email = jsonNode.get("email").asText();
        Date date = new Date();
        User newUser = new User();
        if(!email.isEmpty()) newUser.setUsername(email.split("@")[0]);
        newUser.setBio("Hey! Looking for new Coding Partner");
        if(!gmailId.isEmpty()) newUser.setGmailId(gmailId);
        if(!email.isEmpty()) newUser.setEmail(email);
        newUser.setCreatedAt(date);
        if(!leetcodeUsername.isEmpty()) newUser.setLeetcodeUsername(leetcodeUsername);
        if(!codeforcesUsername.isEmpty()) newUser.setCodeforcesUsername(codeforcesUsername);
        return newUser;
    }

    public User getUserById(long dataUserId) {
        Optional<User> user = userRepository.findById(dataUserId);
        return user.orElse(null);
    }

    public List<Long> findConnectedUsersId(String gmailId) {
        return userRepository.findConnectedUsersId(gmailId);
    }

    public List<String> findGmailIdsByIds(List<Long> userIds) {
        return userRepository.findGmailIdsByIds(userIds);
    }
    public String findUsernameByGmailId(String gmailId) {
        return userRepository.findUsernameByGmailId(gmailId);
    }
}
