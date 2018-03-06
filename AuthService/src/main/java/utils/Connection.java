package utils;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Connection {

	private static Client client = ClientBuilder.newClient();
	private static WebTarget target = client.target(Endpoint.BASE_URL);

	private static void reloadUri() {
		target = null;
		target = client.target(Endpoint.BASE_URL);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> sendRequest(String endpoint, Map<String, Object> params) throws Exception {
		reloadUri();
		target = target.path(endpoint);

		String input = JsonUtil.stringify(params);

		Response response = target.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(input, MediaType.APPLICATION_JSON), Response.class);

		System.out.println(response);
		Map<String, Object> res = (Map<String, Object>) response.readEntity(Map.class);
		return res;
	}

}
