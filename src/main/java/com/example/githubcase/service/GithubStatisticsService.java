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
            ResponseEntity<String> responseEntity = getRepositoryInfo(organization, repository);

            JsonNode jsonNode = getJsonNode(responseEntity);

            List<String> topContributors = getTopContributors(jsonNode, 10, restTemplate, repository);

            return writeToFile("topContributors.txt", topContributors, resourcesPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private JsonNode getJsonNode(ResponseEntity<String> responseEntity) throws JsonProcessingException {
        String responseBody = responseEntity.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode;
    }

    private ResponseEntity<String> getRepositoryInfo(String organization, String repository) {
        String apiUrl = String.format("https://api.github.com/repos/%s/%s/contributors", organization, repository);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class
        );
    }

    private static List<String> getTopContributors(JsonNode jsonNode, int count, RestTemplate restTemplate, String repository) throws JsonProcessingException {
        List<String> topContributors = new ArrayList<>();


        for (int i = 0; i < count && i < jsonNode.size(); i++) {
            JsonNode userNode = jsonNode.get(i);
            String login = userNode.at("/login").asText();

            int contributions = userNode.at("/contributions").asInt();

            ResponseEntity<String> userResponseEntity = getUserInfo(restTemplate, login);

            addTopContributors(userResponseEntity, contributions, topContributors);
        }

        return topContributors;
    }

    private static ResponseEntity<String> getUserInfo(RestTemplate restTemplate, String login) {
        String userApiUrl = "https://api.github.com/users/" + login;
        return restTemplate.exchange(
                userApiUrl,
                HttpMethod.GET,
                null,
                String.class
        );
    }

    private static void addTopContributors(ResponseEntity<String> userResponseEntity, int contributions, List<String> topContributors) throws JsonProcessingException {
        String userResponseBody = userResponseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode userJsonNode = objectMapper.readTree(userResponseBody);
        String username = userJsonNode.at("/login").asText();
        String location = userJsonNode.at("/location").asText();
        String company = userJsonNode.at("/company").asText();

        Contributor contributor = new Contributor(username, location, contributions, company);

        topContributors.add(contributor.toString());
    }


}
