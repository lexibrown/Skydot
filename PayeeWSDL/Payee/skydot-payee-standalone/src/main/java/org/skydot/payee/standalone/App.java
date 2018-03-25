package org.skydot.payee.standalone;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.skydot.paymentservice.PaymentServicePort;
import com.skydot.paymentservice.types.Search;

public class App {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/cxf-requester.xml");
		PaymentServicePort client = (PaymentServicePort) applicationContext.getBean("payeeRequesterBean");

		Search search = new Search();
		search.setSearch("");
		System.out.println("Result: " + client.search(search).getMessage());
	}
}
