package com.connectify.connectify.Controller;

import com.connectify.connectify.Entity.ActiveConnection;
import com.connectify.connectify.Entity.ConnectionRequest;
import com.connectify.connectify.Entity.Rating;
import com.connectify.connectify.Entity.User;
import com.connectify.connectify.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ConnectionController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private ConnectionService connectionService;
    @GetMapping(value = "/find-partners")
    public String getFindPartners(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                                  Model model) throws JsonProcessingException {
        User user = userService.getLoggedInUser(authorizedClient);
        if(user == null){
            JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
            String gmailId = jsonNode.get("sub").asText();
            model.addAttribute("gmailId",gmailId);
            return "redirect:/";
        }
        else{
            List<Rating> relatableUsers = ratingService.findRelatableUsers(user.getId());
            model.addAttribute("ratings", relatableUsers);
            System.out.println(relatableUsers.toString());
            return "findPartner";
        }
    }
    @PostMapping(value = "/send-request")
    @ResponseBody
    public String sendRequest(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                                      @RequestBody Map<String, Long> requestData) throws JsonProcessingException {
        User user = userService.getLoggedInUser(authorizedClient);
        if(user == null){
            return "redirect:/";
        }
        else{
            long dataUserId = requestData.get("data-user-id");
            User requestedUser = userService.getUserById(dataUserId);
            ConnectionRequest connectionRequest = new ConnectionRequest(user,dataUserId);
            requestedUser.getReceivedConnectionRequests().add(connectionRequest);
            userService.saveUserToDB(requestedUser);
            notifyService.notifyUserWithMail(requestedUser);
        }
        return "Request Sent";
    }
    @PostMapping(value = "/accept-request")
    @ResponseBody
    public String acceptRequest(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                              @RequestBody Map<String, Long> requestData) throws JsonProcessingException {
        User user = userService.getLoggedInUser(authorizedClient);
        if(user == null){
            return "redirect:/";
        }
        else{
            long dataUserId = requestData.get("data-user-id");
            User requestedUser = userService.getUserById(dataUserId);
            ActiveConnection activeConnection1 = new ActiveConnection(user.getId(),requestedUser);
            ActiveConnection activeConnection2 = new ActiveConnection(requestedUser.getId(),user);
            user.getActiveConnections().add(activeConnection1);
            connectionService.removeConnectionRequest(user.getId(),requestedUser.getId());
            requestedUser.getActiveConnections().add(activeConnection2);
            userService.saveUserToDB(requestedUser);
            userService.saveUserToDB(user);
            notifyService.notifyUserWithMail(requestedUser);
        }
        return "Request Accepted";
    }
    @GetMapping(value = "/pending-requests")
    public String getPendingRequests(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                                     Model model) throws JsonProcessingException {
        User user = userService.getLoggedInUser(authorizedClient);
        if(user == null){
            JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
            String gmailId = jsonNode.get("sub").asText();
            model.addAttribute("gmailId",gmailId);
            return "redirect:/";
        }
        else{
            List<Rating> pendingRequests = ratingService.findPendingRequest(user.getId());
            model.addAttribute("ratings", pendingRequests);
            System.out.println(pendingRequests.toString());
            return "pendingRequest";
        }
    }
}
