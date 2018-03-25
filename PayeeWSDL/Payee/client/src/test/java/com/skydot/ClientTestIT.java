package com.skydot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/context-requester.xml"})
public class ClientTestIT {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientTestIT.class);
	private static final ObjectMapper objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).configure(JsonParser.Feature.ALLOW_COMMENTS, true);

	@Autowired
	private Client clientBean;

	@Test
	public void testPayees() throws Exception {
		String result = clientBean.search("");
		assertEquals("OK", result);
		LOGGER.info("Response: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
	}
}
