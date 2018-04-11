package com.skydot.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skydot.transfer.gen.model.TransferRequest;
import com.skydot.transfer.gen.model.TransferResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TransferClient {
	
    private ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private RestTemplate template = new RestTemplate();

    public TransferResponse transfer(final TransferRequest request) {
        TransferResponse response = new TransferResponse();

        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            final URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://skydot-bank.azurewebsites.net/host/transfer")
                    .build().toUri();

            log.debug("Calling transfer service. URI: {}, headers: {}", uri, mapper.writeValueAsString(headers));

            HttpEntity<TransferRequest> req = new HttpEntity<>(request, headers);
            ResponseEntity<HashMap> res = template.postForEntity(uri, req, HashMap.class);
            Map body = res.getBody();
            log.info("{}", mapper.writeValueAsString(body));
            response.setMessage((String) body.get("message"));
            if (body.containsKey("error")) {
                response.setStatus(400);
                response.setError((String) body.get("error"));
            }
        } catch (HttpStatusCodeException e) {
            log.error("Calling transfer service failed!", e);
            try {
                Map map = mapper.readValue(e.getResponseBodyAsString(), HashMap.class);
                response.setMessage((String) map.get("message"));
                response.setError((String) map.get("error"));
            } catch (Exception ex) {
                //
            }
            response.setStatus(e.getRawStatusCode());
        } catch (Exception e) {
            log.error("Calling transfer service failed!", e);
            response.setStatus(400);
            response.setMessage("Failure: " + e.getMessage());
		}

        return response;
    }
}
