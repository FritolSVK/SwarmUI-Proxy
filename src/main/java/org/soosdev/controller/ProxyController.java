package org.soosdev.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static org.soosdev.conf.WebConfig.baseSwarmUrl;

@RestController
@RequestMapping("/api")
public class ProxyController {

    private static final Logger logger = LogManager.getLogger(ProxyController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/GetNewSession")
    public Object proxyGetNewSession() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{}", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseSwarmUrl + "/api/getnewsession", // all lowercase
                HttpMethod.POST,
                entity,
                String.class
        );

        logger.info("Status: {}", response.getStatusCode());
        logger.info("Body: {}", response.getBody());

        return mapper.readValue(response.getBody(), Map.class);
    }

    @PostMapping("/GenerateText2Image")
    public Object proxyGenerateText2Image(@RequestBody Map<String, Object> body) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(body), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseSwarmUrl + "/api/generatetext2image", // all lowercase
                HttpMethod.POST,
                entity,
                String.class
        );

        logger.info("Status: {}", response.getStatusCode());
        logger.info("Body: {}", response.getBody());


        return mapper.readValue(response.getBody(), Map.class);
    }

    @PostMapping("/ListImages")
    public Object proxyListImages(@RequestBody Map<String, Object> body) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(body), headers);
        ResponseEntity<String> response = restTemplate.exchange(
                baseSwarmUrl + "/api/listimages",
                HttpMethod.POST,
                entity,
                String.class
        );
        logger.info("Status: {}", response.getStatusCode());
        logger.info("Body: {}", response.getBody());
        return mapper.readValue(response.getBody(), Map.class);
    }

    @PostMapping("/RestartBackends")
    public Object proxyRestartBackends(@RequestBody Map<String, Object> body) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(body), headers);
        ResponseEntity<String> response = restTemplate.exchange(
                baseSwarmUrl + "/api/restartbackends",
                HttpMethod.POST,
                entity,
                String.class
        );
        logger.info("Status: {}", response.getStatusCode());
        logger.info("Body: {}", response.getBody());
        return mapper.readValue(response.getBody(), Map.class);
    }
}
