package com.skydot.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skydot.paymentservice.PaymentServicePort;
import com.skydot.paymentservice.types.ObjectFactory;
import com.skydot.paymentservice.types.Payees;
import com.skydot.paymentservice.types.Payees.Payee;
import com.skydot.paymentservice.types.Search;
import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server implements PaymentServicePort {
	private final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	private Payees response;

	public Server() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ObjectMapper objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		ObjectFactory factory = new ObjectFactory();

		response = factory.createPayees();
		response.setCode(BigInteger.valueOf(0L));
		response.setMessage("OK");
		List<Payee> payees = response.getPayee();

		try {
			URI uri = classLoader.getResource("Payees.json").toURI();
			String json = new String(Files.readAllBytes(Paths.get(uri)));
			List<Payee> payeesList = objectMapper.readValue(json, new TypeReference<List<Payee>>(){});
			payees.addAll(payeesList);
		} catch (Exception e) {
			Payee payee1 = new Payee();
			payee1.setId(BigInteger.valueOf(1L));
			payee1.setName("Rogers");
			payees.add(payee1);

			Payee payee2 = new Payee();
			payee2.setId(BigInteger.valueOf(2L));
			payee2.setName("Telus");
			payees.add(payee2);

			Payee payee3 = new Payee();
			payee3.setId(BigInteger.valueOf(3L));
			payee3.setName("Bell");
			payees.add(payee3);
		}
	}

	public Payees search(Search search) {
		if (search == null || StringUtils.isEmpty(search.getSearch())) {
			return response;
		}

		Payees rs = new Payees();

		try {
			String[] ss = search.getSearch().trim().replaceAll("\\s+?"," ").split(" ");
			List<Payee> payeesList = rs.getPayee();
			for (Payee payee : response.getPayee()) {
				boolean found = true;
				for (String s : ss) {
					Pattern p = Pattern.compile(s, Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher(payee.getName());
					if (!m.find()) {
						found = false;
						break;
					}
				}
				if (found) {
					payeesList.add(payee);
				}
			}
			if (payeesList.size() == 0) {
				rs.setCode(BigInteger.valueOf(1L));
				rs.setMessage("Could not find any Payees");
			} else {
				rs.setCode(BigInteger.valueOf(0L));
				rs.setMessage("OK");
			}
		} catch (Exception e) {
			rs.setCode(BigInteger.valueOf(2L));
			rs.setMessage("Could not find any Payees");
			LOGGER.info("Error: {}", e.getMessage());
		}

		return rs;
	}
}