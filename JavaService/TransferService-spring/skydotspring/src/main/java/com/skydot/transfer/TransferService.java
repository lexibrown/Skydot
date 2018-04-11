package com.skydot.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skydot.transfer.gen.model.TransferRequest;
import com.skydot.transfer.gen.model.TransferResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@AllArgsConstructor
@Validated
public class TransferService {
	
    TransferClient transferClient;

    private ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    @RequestMapping(path = "transfer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public TransferResponse transfer(@RequestBody TransferRequest request) {

        try {
            log.debug("[REQUEST] {}", mapper.writeValueAsString(request));
            TransferResponse response = transferClient.transfer(request);
            log.debug("[RESPONSE] {}", mapper.writeValueAsString(response));
            return response;
        } catch (Exception e) {
            TransferResponse transferResponse = new TransferResponse();
            transferResponse.setStatus(400);
            transferResponse.setMessage("FAIL");
            return transferResponse;
        }
    }
}
