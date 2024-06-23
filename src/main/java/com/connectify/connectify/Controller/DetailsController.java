package com.connectify.connectify.Controller;

import com.connectify.connectify.Entity.Rating;
import com.connectify.connectify.Entity.User;
import com.connectify.connectify.Service.AuthService;
import com.connectify.connectify.Service.RatingService;
import com.connectify.connectify.Service.ThirdPartyService;
import com.connectify.connectify.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DetailsController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private RatingService ratingService;

    @GetMapping(value = "/home")
    public String getHome(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                          RedirectAttributes attributes, Model model) throws JsonProcessingException {
        User user = userService.getLoggedInUser(authorizedClient);
        if(user == null){
            JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
            String gmailId = jsonNode.get("sub").asText();
            model.addAttribute("gmailId",gmailId);
            return "redirect:/";
        }
        else {
            String leetcodeUsername = user.getLeetcodeUsername();
            if (leetcodeUsername != null && !leetcodeUsername.isEmpty()) {
                JsonNode jsonNode = thirdPartyService.getLeetcodeDetails(leetcodeUsername);
                if (jsonNode != null) {
                    addLeetcodeToDetailsPage(jsonNode, model);
                    if (jsonNode.get("ranking").asInt() != 0) {
                        Rating rating = ratingService.findRatingByUser(user.getId());
                        if (rating != null) {
                            rating.setRanking(jsonNode.get("ranking").asInt());
                            ratingService.saveRating(rating);
                        } else {
                            Rating newRating = new Rating();
                            newRating.setUser(user);
                            newRating.setRanking(jsonNode.get("ranking").asInt());
                            ratingService.saveRating(newRating);
                        }
                    }
                    else{
                        Rating rating = ratingService.findRatingByUser(user.getId());
                        if (rating != null) {
                            rating.setRanking(0);
                            ratingService.saveRating(rating);
                        }
                    }
                }
            }
            String codeforcesUsername = user.getCodeforcesUsername();
            if (codeforcesUsername != null && !codeforcesUsername.isEmpty()) {
                JsonNode jsonNode = thirdPartyService.getCodeforcesDetails(codeforcesUsername);
                if (jsonNode != null) {
                    addCodeforcesToDetailsPage(jsonNode.get("result"), model);
                    if (jsonNode.get("result") != null && jsonNode.get("result").get(0).get("rating") != null) {
                        Rating rating = ratingService.findRatingByUser(user.getId());
                        if (rating != null) {
                            rating.setRating(jsonNode.get("result").get(0).get("rating").asInt());
                            ratingService.saveRating(rating);
                        } else {
                            Rating newRating = new Rating();
                            newRating.setUser(user);
                            newRating.setRating(jsonNode.get("result").get(0).get("rating").asInt());
                            ratingService.saveRating(newRating);
                        }
                    }
                }
                else{
                    Rating rating = ratingService.findRatingByUser(user.getId());
                    if (rating != null) {
                        rating.setRating(0);
                        ratingService.saveRating(rating);
                    }
                }
            }
            List<Rating> activeConnections = ratingService.findAllActiveUsers(user.getId());
            JsonNode jsonNode = authService.getUserDataByAuthorizedClient(authorizedClient);
            String gmailId = jsonNode.get("sub").asText();
            model.addAttribute("gmailId",gmailId);
            model.addAttribute("ratings", activeConnections);
            return "details";
        }
    }
    public void addLeetcodeToDetailsPage(JsonNode jsonNode, Model model){
        if(jsonNode.get("totalSolved") != null){
            model.addAttribute("totalSolved", jsonNode.get("totalSolved").asText());
        }
        if(jsonNode.get("totalQuestions") != null){
            model.addAttribute("totalQuestions", jsonNode.get("totalQuestions").asText());
        }
        if(jsonNode.get("easySolved") != null){
            model.addAttribute("easySolved", jsonNode.get("easySolved").asText());
        }
        if(jsonNode.get("totalEasy") != null){
            model.addAttribute("totalEasy", jsonNode.get("totalEasy").asText());
        }
        if(jsonNode.get("mediumSolved") != null){
            model.addAttribute("mediumSolved", jsonNode.get("mediumSolved").asText());
        }
        if(jsonNode.get("totalMedium") != null){
            model.addAttribute("totalMedium", jsonNode.get("totalMedium").asText());
        }
        if(jsonNode.get("hardSolved") != null){
            model.addAttribute("hardSolved", jsonNode.get("hardSolved").asText());
        }
        if(jsonNode.get("totalHard") != null){
            model.addAttribute("totalHard", jsonNode.get("totalHard").asText());
        }
        if(jsonNode.get("ranking") != null){
            model.addAttribute("ranking", jsonNode.get("ranking").asText());
        }
    }
    private void addCodeforcesToDetailsPage(JsonNode jsonNode, Model model) {
        if(jsonNode.get(0).get("rating") != null){
            model.addAttribute("rating", jsonNode.get(0).get("rating").asText());
        }
        if(jsonNode.get(0).get("rank") != null){
            model.addAttribute("rank", jsonNode.get(0).get("rank").asText());
        }
        if(jsonNode.get(0).get("maxRating") != null){
            model.addAttribute("maxRating", jsonNode.get(0).get("maxRating").asText());
        }
        if(jsonNode.get(0).get("maxRank") != null){
            model.addAttribute("maxRank", jsonNode.get(0).get("maxRank").asText());
        }
        if(jsonNode.get(0).get("contribution") != null){
            model.addAttribute("contribution", jsonNode.get(0).get("contribution").asText());
        }
    }
}


