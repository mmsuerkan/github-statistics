package com.example.githubcase.service;

import com.example.githubcase.model.Contributor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.githubcase.util.FileWriter.writeToFile;

@Service
public class GithubStatisticsService {


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String resourcesPath;

    public GithubStatisticsService(RestTemplate restTemplate, ObjectMapper objectMapper,
                                   @Value("${github.resources.path}") String resourcesPath) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.resourcesPath = resourcesPath;
    }

    public boolean getTop10Contributors(String organization, String repository) {
        try {
            String apiUrl = String.format("https://api.github.com/repos/%s/%s/contributors", organization, repository);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    String.class
            );

            String responseBody = responseEntity.getBody();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            List<String> topContributors = getTopContributors(jsonNode, 10, restTemplate, repository);

            return writeToFile("topContributors.txt", topContributors,resourcesPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static List<String> getTopContributors(JsonNode jsonNode, int count, RestTemplate restTemplate, String repository) throws JsonProcessingException {
        List<String> topContributors = new ArrayList<>();


        for (int i = 0; i < count && i < jsonNode.size(); i++) {
            JsonNode userNode = jsonNode.get(i);
            String login = userNode.at("/login").asText();

            int contributions = userNode.at("/contributions").asInt();

            String userApiUrl = "https://api.github.com/users/" + login;
            ResponseEntity<String> userResponseEntity = restTemplate.exchange(
                    userApiUrl,
                    HttpMethod.GET,
                    null,
                    String.class
            );

            String userResponseBody = userResponseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode userJsonNode = objectMapper.readTree(userResponseBody);
            String username = userJsonNode.at("/login").asText();
            String location = userJsonNode.at("/location").asText();
            String company = userJsonNode.at("/company").asText();

            Contributor contributor = new Contributor(username,location,contributions,company);

            topContributors.add(contributor.toString());
        }

        return topContributors;
    }


}
