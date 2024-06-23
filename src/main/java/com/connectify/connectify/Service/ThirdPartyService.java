package com.connectify.connectify.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ThirdPartyService {
    @Autowired
    private RestTemplate restTemplate;

    public JsonNode getLeetcodeDetails(String leetcodeUsername) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response;
        String leetCodeInfoEndpoint = "https://leetcode-stats-api.herokuapp.com/" + leetcodeUsername;
        try{
            response = restTemplate.exchange(leetCodeInfoEndpoint, HttpMethod.GET,
                    new HttpEntity<>(httpHeaders), String.class);
            if(response.getStatusCode().value() == 200){
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                System.out.println(responseBody);
                return jsonNode;
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            return null;
        }
    }

    public JsonNode getCodeforcesDetails(String codeforcesUsername) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response;
        String codeforcesInfoEndpoint = "https://codeforces.com/api/user.info?handles=" + codeforcesUsername + "&checkHistoricHandles=false";
        try {
            response = restTemplate.exchange(codeforcesInfoEndpoint, HttpMethod.GET,
                    new HttpEntity<>(httpHeaders), String.class);
            String responseBody = response.getBody();
            System.out.println("CF Response " + responseBody);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode;
        }
        catch(Exception e){
            return null;
        }
    }
}
