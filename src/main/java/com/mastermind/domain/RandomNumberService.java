package com.mastermind.domain;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RandomNumberService {

    private final RestTemplate restTemplate;

    public RandomNumberService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateSecretCode() {
        String url = "https://www.random.org/integers/?num=4&min=0&max=7&col=1&base=10&format=plain&rnd=new";
        String result = restTemplate.getForObject(url, String.class);
        return result != null ? result.replaceAll("\\s+", "") : "";
    }
}
