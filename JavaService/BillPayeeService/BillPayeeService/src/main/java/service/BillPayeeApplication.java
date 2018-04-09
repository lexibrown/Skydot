package service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.BillPayeeUtil;
import utils.JsonUtil;
import utils.Variables;

@Path("/bill/payee")
@Produces(MediaType.APPLICATION_JSON)
public class BillPayeeApplication {

	private static final Logger log = LogManager.getLogger(BillPayeeApplication.class);

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello";
	}

	@POST
	public Response getPayees(HashMap<String, Object> params) {
		try {
			Map<String, Object> response = BillPayeeUtil.getPayees();
			if (response.containsKey("error")) {
				return Response.status((int) response.get("status")).entity(JsonUtil.stringify(response)).build();
			}
			return Response.ok(JsonUtil.stringify(response), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	@POST
	@Path("/search")
	public Response searchPayees(HashMap<String, Object> params) {
		try {
			if (!params.containsKey(Variables.SEARCH)) {
				return errorResponse();
			}

			String search = params.get(Variables.SEARCH).toString();

			Map<String, Object> response = BillPayeeUtil.search(search);
			if (response.containsKey("error")) {
				return Response.status((int) response.get("status")).entity(JsonUtil.stringify(response)).build();
			}
			return Response.ok(JsonUtil.stringify(response), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return crashResponse();
		}
	}

	public Response errorResponse() throws Exception {
		return Response.status(Response.Status.BAD_REQUEST).entity(JsonUtil.errorJson("BILL-1000", "Invalid request"))
				.build();
	}

	public Response crashResponse() {
		try {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(JsonUtil.errorJson("BILL-5000", "Something went wrong. Please try again.")).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}