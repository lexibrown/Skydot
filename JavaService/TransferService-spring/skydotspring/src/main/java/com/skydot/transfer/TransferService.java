package com.skydot.transfer;

import com.skydot.transfer.gen.model.TransferRequest;
import com.skydot.transfer.gen.model.TransferResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
@Validated
public class TransferService {
    TransferClient transferClient;

    @RequestMapping(path = "transfer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public TransferResponse transfer(@RequestBody TransferRequest request) {

        try {
            return transferClient.transfer(request);
        } catch (Exception e) {
            TransferResponse transferResponse = new TransferResponse();
            transferResponse.setStatus(400);
            transferResponse.setMessage("FAIL");
            return transferResponse;
        }
    }
}
