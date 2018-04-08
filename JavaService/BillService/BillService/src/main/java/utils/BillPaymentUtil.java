package utils;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BillPaymentUtil {

	public static final String BASE_URL = "http://host-gateway";
	public static final String PATH = "/host/bill/payee";

	public static Map<String, Object> submitPayment(String userId, int fromId, int payee, double amount, String currency)
			throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put(Variables.USER_ID, userId);
		params.put(Variables.FROM, fromId);
		params.put(Variables.PAYEE, payee);
		params.put(Variables.AMOUNT, amount);
		params.put(Variables.CURRENCY, currency);
		
		return sendRequest(params);
	}

	private static Client client = ClientBuilder.newClient();
	private static WebTarget target = client.target(BASE_URL);

	private static void reloadUri() {
		target = null;
		target = client.target(BASE_URL);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> sendRequest(Map<String, Object> params) throws Exception {
		reloadUri();
		target = target.path(PATH);

		String input = JsonUtil.stringify(params);

		Response response = target.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(input, MediaType.APPLICATION_JSON), Response.class);

		System.out.println(response);
		Map<String, Object> res = (Map<String, Object>) response.readEntity(Map.class);
		
		res.put("status", response.getStatus());
		
		return res;
	}
	
}