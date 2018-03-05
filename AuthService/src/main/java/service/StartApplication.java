package service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class StartApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		// register root resource
		classes.add(Hello.class);
		classes.add(TestApplication.class);
		
		classes.add(UserApplication.class);
		classes.add(AccountApplication.class);
		classes.add(TransfersApplication.class);
		classes.add(BillPaymentApplication.class);
		
		return classes;
	}
}