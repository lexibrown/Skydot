package com.skydot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skydot.service.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.BeforeClass;
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
public class ClientTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientTest.class);
	private static final String ENDPOINT_ADDRESS = "http://localhost:9090/payees";
	private static final ObjectMapper objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).configure(JsonParser.Feature.ALLOW_COMMENTS, true);

	@Autowired
	private Client client;

	@BeforeClass
	public static void setUpBeforeClass() {
		createServerEndpoint();
	}

	@Test
	public void testSearch() throws Exception {
		String result = client.search("");
		assertEquals("OK", result);
		LOGGER.info("Response: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
	}

	private static void createServerEndpoint() {
		// use a CXF JaxWsServerFactoryBean to create JAX-WS endpoints
		JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();

		// set the Client implementation
		Server implementor = new Server();
		jaxWsServerFactoryBean.setServiceBean(implementor);

		// set the address at which the Client endpoint will be exposed
		jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);

		// create the server
		jaxWsServerFactoryBean.create();
	}
}