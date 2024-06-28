package com.connectify.connectify.Controller;

import com.connectify.connectify.Entity.User;
import com.connectify.connectify.Entity.UserDao;
import com.connectify.connectify.Service.AuthService;
import com.connectify.connectify.Service.ThirdPartyService;
import com.connectify.connectify.Service.UserDaoService;
import com.connectify.connectify.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuthService authService;

    @Autowired
    private ThirdPartyService thirdPartyService;
    @Autowired
    private UserDaoService userDaoService;

    @PostMapping(value = "/submit")
    public String handleHomeFormSubmit(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
            @RequestParam("leetcodeUsername") String leetcodeUsername,
                                       @RequestParam("codeforcesUsername") String codeforcesUsername, Model model) throws JsonProcessingException {
        User user = userService.getLoggedInUser(authorizedClient);
        if (user == null) {
            User newUser = userService.createNewUser(authorizedClient,leetcodeUsername,codeforcesUsername);
            userService.saveUserToDB(newUser);
            UserDao userDao = new UserDao();
            userDao.setGmailId(newUser.getGmailId());
            userDao.setUsername(newUser.getUsername());
            userDaoService.saveUser(userDao);

        }
        else{
            if(!leetcodeUsername.isEmpty()) user.setLeetcodeUsername(leetcodeUsername);
            if(!codeforcesUsername.isEmpty()) user.setCodeforcesUsername(codeforcesUsername);
            userService.saveUserToDB(user);
        }
        return "redirect:/home";

    }
    @PostMapping(value = "/save-profile")
    public String postEditProfile(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                                  @RequestParam("username") String username,
                                  @RequestParam("bio") String bio,
                                  @RequestParam("profileImage") MultipartFile profileImage,
                                  @RequestParam("leetcodeUsername") String leetcodeUsername,
                                  @RequestParam("codeforceUsername") String codeforceUsername,
                                  Model model) throws IOException {
        User user = userService.getLoggedInUser(authorizedClient);
        if(user == null){
            JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
            String gmailId = jsonNode.get("sub").asText();
            model.addAttribute("gmailId",gmailId);
            return "redirect:/";
        }
        else{
            //String fileName = StringUtils.cleanPath(Objects.requireNonNull(profileImage.getOriginalFilename()));
            if(!username.isEmpty()) user.setUsername(username);
            if(!bio.isEmpty()) user.setBio(bio);
            if(!profileImage.isEmpty()) user.setProfileImage(Base64.getEncoder().encodeToString(profileImage.getBytes()));
            if(!leetcodeUsername.isEmpty()) user.setLeetcodeUsername(leetcodeUsername);
            if(!codeforceUsername.isEmpty()) user.setCodeforcesUsername(codeforceUsername);
            userService.saveUserToDB(user);
            UserDao userDao = userDaoService.findByGmailId(user.getGmailId());
            userDao.setUsername(user.getUsername());
            userDaoService.saveUser(userDao);
            return "redirect:/home";
        }
    }

    @GetMapping(value = "/edit-profile")
    public String getEditProfile(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                                 Model model) throws JsonProcessingException {
        User user = userService.getLoggedInUser(authorizedClient);
        if(user == null){
            JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
            String gmailId = jsonNode.get("sub").asText();
            model.addAttribute("gmailId",gmailId);
            return "redirect:/";
        }
        else{
            model.addAttribute("username",user.getUsername());
            model.addAttribute("bio",user.getBio());
            model.addAttribute("leetcodeUsername",user.getLeetcodeUsername());
            model.addAttribute("codeforcesUsername",user.getCodeforcesUsername());
        }
        return "editProfile";
    }



}
