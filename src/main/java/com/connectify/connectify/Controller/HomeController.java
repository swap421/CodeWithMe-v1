package com.connectify.connectify.Controller;

import com.connectify.connectify.Entity.User;
import com.connectify.connectify.Service.AuthService;
import com.connectify.connectify.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String getUsernameForm(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient, Model model) throws JsonProcessingException {
        User user = userService.getLoggedInUser(authorizedClient);
        if(user == null){
            JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
            String gmailId = jsonNode.get("sub").asText();
            model.addAttribute("gmailId",gmailId);
            return "homepage";
        }
        else{
            return "redirect:/home";
        }
    }
}
