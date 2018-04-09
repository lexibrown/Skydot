package utils;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

	private static final Logger log = LogManager.getLogger(LoggingFilter.class);

	@SuppressWarnings("unchecked")
	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		if (isJson(request)) {
			ContainerRequest cr = (ContainerRequest) request;
			cr.bufferEntity();
			Map<String, Object> json = cr.readEntity(Map.class);
			log.info("[REQUEST] [{}] (Path: {}) {}", request.getMethod(), request.getUriInfo().getPath(), json);
		} else {
			log.info("[REQUEST] [{}] (Path: {})", request.getMethod(), request.getUriInfo().getPath());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		if (isJson(request)) {
			ContainerRequest cr = (ContainerRequest) request;
			cr.bufferEntity();
			Map<String, Object> json = cr.readEntity(Map.class);
			log.info("[REQUEST] [{}] (Path: {}) {}", request.getMethod(), request.getUriInfo().getPath(), json);
		} else {
			log.info("[REQUEST] [{}] (Path: {})", request.getMethod(), request.getUriInfo().getPath());
		}

		ContainerResponse cr = (ContainerResponse) response;
		Object o = cr.getEntity();
		int status = cr.getStatus();
		if (o != null) {
			log.info("[RESPONSE] [{}] {}", status, o.toString());
		} else {
			log.info("[RESPONSE] [{}]", status);
		}
	}

	boolean isJson(ContainerRequestContext request) {
		try {
			return request.getMediaType().toString().contains("application/json");
		} catch (Exception e) {
			return false;
		}
	}

}