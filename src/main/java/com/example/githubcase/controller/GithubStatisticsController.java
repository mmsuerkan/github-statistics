package com.example.githubcase.controller;

import com.example.githubcase.service.GithubStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubStatisticsController {

    private GithubStatisticsService githubStatisticsService;

    @Value("${github.resources.path}")
    private String fileLocation;

    @Autowired
    public GithubStatisticsController(GithubStatisticsService githubStatisticsService) {
        this.githubStatisticsService = githubStatisticsService;
    }

    @GetMapping("/getTop10Contributors/{organization}/{repository}")
    public ResponseEntity<String> getTop10Contributors(
            @PathVariable String organization,
            @PathVariable String repository) {
        boolean result = githubStatisticsService.getTop10Contributors(organization, repository);

        if (result) {
            return ResponseEntity.ok("Successfully written to file under: " + fileLocation);
        } else {
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }

}
