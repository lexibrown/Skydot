package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import data.Account;
import data.AccountDetail;
import data.Transaction;
import utils.JsonUtil;

@Path("/test")
public class TestApplication {	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello Jersey";
	}
	
	@POST
	@Path("/auth/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	public String login(HashMap<String, Object> params) {
		System.out.println(params);
		
		params = new HashMap<>();
		params.put("token", "token");
		
		try {
			return JsonUtil.stringify(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}

	@POST
	@Path("/account")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	public String account(HashMap<String, Object> params) {
		System.out.println(params);
		
		try {
			List<Account> items = new ArrayList<>();
			
			Account a1 = new Account();
			a1.setBalanceCAD(10.20);
			a1.setBalanceUSD(3.02);
			a1.setId(1234);
			a1.setName("Bank Account");
			a1.setType("Banking");
			
			Account a2 = new Account();
			a2.setBalanceCAD(1000.00);
			a2.setBalanceUSD(0.0);
			a2.setId(5678);
			a2.setName("Visa");
			a2.setType("Borrowing");
			
			Account a3 = new Account();
			a3.setBalanceCAD(0.0);
			a3.setBalanceUSD(26.72);
			a3.setId(9000);
			a3.setName("Mutual Funds");
			a3.setType("Investing");
			
			items.add(a1);
			items.add(a2);
			items.add(a3);
			
			params = new HashMap<>();
			params.put("accounts", JsonUtil.stringify(items));
			
			return JsonUtil.stringify(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}
	
	@POST
	@Path("/account/{account_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	public String accountDetails(HashMap<String, Object> params, @PathParam("{account_id}") int account) {
		System.out.println(params);
		
		try {
			
			AccountDetail a1 = new AccountDetail();
			a1.setBalanceCAD(10.20);
			a1.setBalanceUSD(3.02);
			a1.setId(1234);
			a1.setName("Bank Account");
			a1.setType("Banking");

			List<Transaction> history = new ArrayList<>();
			
			Transaction t1 = new Transaction();
			t1.setAmount(100);
			t1.setDate("May 5th");
			t1.setId(1);
			t1.setName("Best Buy");
			history.add(t1);
			
			Transaction t2 = new Transaction();
			t2.setAmount(-10);
			t2.setDate("May 6th");
			t2.setId(2);
			t2.setName("Tim Hortons");
			history.add(t2);
			
			a1.setHistory(history);
			
			params = new HashMap<>();
			params.put("account", JsonUtil.stringify(a1));
			
			return JsonUtil.stringify(a1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonUtil.fail(e);
		}
	}
	
}
