package org.soosdev;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    private static final Logger logger = LogManager.getLogger(ProxyController.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseSwarmUrl = "http://localhost:7801/api"; // Adjust to your SwarmUI endpoint

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/GetNewSession")
    public Object proxyGetNewSession() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{}", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseSwarmUrl + "/getnewsession", // all lowercase
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
                baseSwarmUrl + "/generatetext2image", // all lowercase
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
                baseSwarmUrl + "/listimages",
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
                baseSwarmUrl + "/restartbackends",
                HttpMethod.POST,
                entity,
                String.class
        );
        logger.info("Status: {}", response.getStatusCode());
        logger.info("Body: {}", response.getBody());
        return mapper.readValue(response.getBody(), Map.class);
    }

    @GetMapping("/View/local/raw")
    public ResponseEntity<byte[]> proxyFetchImage(@RequestParam("filename") String filename) {
        String url = baseSwarmUrl.replace("/api", "") + "/View/local/raw/" + filename;
        logger.info("Fetching image: {} from URL: {}", filename, url);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                byte[].class
        );
        logger.info("Image fetch status: {}", response.getStatusCode());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or detect type
        return new ResponseEntity<>(response.getBody(), headers, response.getStatusCode());
    }
}
