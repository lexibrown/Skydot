package com.skydot.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skydot.paymentservice.PaymentServicePort;
import com.skydot.paymentservice.types.Payees;
import com.skydot.paymentservice.types.Search;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class ServerTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerTest.class);
	private static final String ENDPOINT_ADDRESS = "http://localhost:9080/PaymentService";
	private static final ObjectMapper objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	private static PaymentServicePort paymentServicePortProxy;

	@BeforeClass
	public static void setUpBeforeClass() {
		createServerEndpoint();
		paymentServicePortProxy = createClientProxy();
	}

	@Test
	public void testProxy() throws Exception {
		Search search = new Search();
		search.setSearch("");

		Payees response = paymentServicePortProxy.search(search);
		LOGGER.info("Response: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
		assertEquals(0L, response.getCode().longValue());
	}

	@Test
	public void testEmpty() throws Exception {
		Search search = new Search();
		search.setSearch("dbrrrwntnemeymetmulrikrabanmu");

		Payees response = paymentServicePortProxy.search(search);
		LOGGER.info("Response: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
		assertEquals(1L, response.getCode().longValue());
	}

	@Test
	public void testRegex() throws Exception {
		Search search = new Search();
		search.setSearch("*");

		Payees response = paymentServicePortProxy.search(search);
		LOGGER.info("Response: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
		assertEquals(2L, response.getCode().longValue());
	}

	private static void createServerEndpoint() {
		// use a CXF JaxWsServerFactoryBean to create JAX-WS endpoints
		JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();

		// set the PaymentService implementation
		Server implementor = new Server();
		jaxWsServerFactoryBean.setServiceBean(implementor);

		// set the address at which the PaymentService endpoint will be exposed
		jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);

		// create the server
		jaxWsServerFactoryBean.create();
	}

	private static PaymentServicePort createClientProxy() {
		// create a CXF JaxWsProxyFactoryBean for creating JAX-WS proxies
		JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();

		// // set the PaymentService portType class
		jaxWsProxyFactoryBean.setServiceClass(PaymentServicePort.class);
		// set the address at which the PaymentService endpoint will be called
		jaxWsProxyFactoryBean.setAddress(ENDPOINT_ADDRESS);

		// create a JAX-WS proxy for the PaymentService portType
		return (PaymentServicePort) jaxWsProxyFactoryBean.create();
	}
}
