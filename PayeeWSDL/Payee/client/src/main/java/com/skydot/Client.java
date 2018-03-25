package com.skydot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skydot.paymentservice.PaymentServicePort;
import com.skydot.paymentservice.types.Payees;
import com.skydot.paymentservice.types.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Client {
	private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
	private final ObjectMapper objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).configure(JsonParser.Feature.ALLOW_COMMENTS, true);

	@Autowired
	private PaymentServicePort payeeRequesterBean;

	public String search(String search) {
		Search payeeRq = new Search();
		payeeRq.setSearch(search);
		Payees payeesResponse = payeeRequesterBean.search(payeeRq);

		try {
			LOGGER.info("Response: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payeesResponse));
		} catch (Exception e) {
			//
		}
		return payeesResponse.getMessage();
	}
}