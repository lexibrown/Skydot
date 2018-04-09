package service;

import javax.ws.rs.core.Response;

import utils.JsonUtil;

public class BaseApplication {

	public static final String SERVICE = "MOBL";
	
	public Response noTokenResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST).entity(JsonUtil.errorJson(SERVICE + "-1000", "No token provided.")).build();
	}
	
	public Response invalidResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST).entity(JsonUtil.errorJson(SERVICE + "-2000", "Invalid request.")).build();
	}

	public Response crashResponse() {
		try {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(JsonUtil.errorJson(SERVICE + "-5000", "Something went wrong. Please try again.")).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
