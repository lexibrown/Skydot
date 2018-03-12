package server;

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

		classes.add(UserApplication.class);
		classes.add(Hello.class);

		return classes;
	}
}