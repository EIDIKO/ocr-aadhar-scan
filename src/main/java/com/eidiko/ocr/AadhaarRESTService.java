package com.eidiko.ocr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AadhaarRESTService {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${auth.header}")
    private String authHeader;

    private final RestTemplate restTemplate;

    public AadhaarRESTService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void extractInfo(String filePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", authHeader);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("filePath", filePath);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        // Handle response if needed
        if (response.getStatusCode() == HttpStatus.OK) {
            // Success
            System.out.println("Request successful!");
        } else {
            // Handle failure
            System.out.println("Request failed with status code: " + response.getStatusCode());
        }
    }
}
