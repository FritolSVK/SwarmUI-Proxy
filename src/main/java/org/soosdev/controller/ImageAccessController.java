package org.soosdev.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;

import static org.soosdev.conf.WebConfig.baseSwarmUrl;

@RestController
@RequestMapping
public class ImageAccessController {

	private static final Logger logger = LogManager.getLogger(ImageAccessController.class);
	private final RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/View/local/raw/**")
	public ResponseEntity<byte[]> proxyFetchImage(HttpServletRequest request) {
		String filename = new AntPathMatcher().extractPathWithinPattern(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(),request.getRequestURI());
		String url = baseSwarmUrl + "/View/local/raw/" + filename;
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
