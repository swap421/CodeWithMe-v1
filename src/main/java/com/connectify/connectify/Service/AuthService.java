package com.connectify.connectify.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    public JsonNode getUserDataByAuthorizedClient(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) throws JsonProcessingException {
        OAuth2AccessToken oAuth2AccessToken = authorizedClient.getAccessToken();
        String accessToken = oAuth2AccessToken.getTokenValue();
        String userInfoEndpointUri = "https://www.googleapis.com/oauth2/v3/userinfo";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(accessToken);
        ResponseEntity<String> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.POST,
                new HttpEntity<>(httpHeaders), String.class);
        if(response.getStatusCode().value() == 200) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode;
        }
        else{
            return null;
        }
    }
}
