package com.comp.lexi.skydot.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.comp.lexi.skydot.database.UserDataStore;
import com.comp.lexi.skydot.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestUtil {

    private Context context;
    private Map<String, Object> params;

    public static synchronized RequestUtil getInstance(Context context) {
        return new RequestUtil(context);
    }

    private RequestUtil(Context context) {
        this.context = context;
    }

    private RequestQueue getRequestQueue() {
        return Volley.newRequestQueue(context.getApplicationContext());
    }

    private <T> void addToRequestQueue(Request<T> req) {
        System.out.println("///////////////////////////////////////////////////");
        System.out.println("//////////////////AAADDDDDDD//////////////////////");
        System.out.println("///////////////////////////////////////////////////");

        req.setRetryPolicy(Variables.retryPolicy);
        getRequestQueue().add(req);
    }

    private Map<String, Object> getParams() {
        return params;
    }

    private void makeRequest(final VolleyCallBack callback, int method, String url) {
        JSONObject requestParams = new JSONObject(getParams());

        JsonObjectRequest req = new JsonObjectRequest(method, Variables.BASE_URL + url, requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("///////////////////////////////////////////////////");
                        System.out.println(response);
                        System.out.println(response.toString());
                        System.out.println("///////////////////////////////////////////////////");

                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("///////////////////////////////////////////////////");
                        System.out.println(error.getMessage());
                        error.printStackTrace();
                        System.out.println("///////////////////////////////////////////////////");

                        callback.onFailure(error.getMessage());
                    }
                }
        );
        addToRequestQueue(req);
    }

    public void login(final VolleyCallBack callback, String username, String password) {
        params = new HashMap<>();
        params.put(context.getString(R.string.USERNAME), username);
        params.put(context.getString(R.string.PASSWORD), password);
        makeRequest(callback, Request.Method.POST, Variables.LOGIN);
    }

    public void logout(final VolleyCallBack callback) {
        params = new HashMap<>();
        params.put(context.getString(R.string.TOKEN), UserDataStore.getDataStore().getToken());
        makeRequest(callback, Request.Method.POST, Variables.LOGOUT);
    }

    public void register(final VolleyCallBack callback, String username, String password) {
        params = new HashMap<>();
        params.put(context.getString(R.string.USERNAME), username);
        params.put(context.getString(R.string.PASSWORD), password);
        makeRequest(callback, Request.Method.POST, Variables.CREATE);
    }

    public void deleteProfile(final VolleyCallBack callback, String password) {
        params = new HashMap<>();
        params.put(context.getString(R.string.USERNAME), UserDataStore.getDataStore().getUser());
        params.put(context.getString(R.string.PASSWORD), password);
        makeRequest(callback, Request.Method.POST, Variables.DELETE);
    }

    public void getAccountSummary(final VolleyCallBack callback) {
        params = new HashMap<>();
        params.put(context.getString(R.string.TOKEN), UserDataStore.getDataStore().getToken());
        makeRequest(callback, Request.Method.POST, Variables.ACCOUNT);
    }

    public void getAccountDetails(final VolleyCallBack callback, int accountId) {
        params = new HashMap<>();
        params.put(context.getString(R.string.TOKEN), UserDataStore.getDataStore().getToken());
        makeRequest(callback, Request.Method.POST, String.format(Locale.getDefault(), Variables.ACCOUNT_DETAILS, accountId));
    }

    public void submitTransfer(final VolleyCallBack callback, Map<String, Object> params) {
        this.params = params;
        params.put(context.getString(R.string.TOKEN), UserDataStore.getDataStore().getToken());
        makeRequest(callback, Request.Method.POST, Variables.TRANSFER);
    }

    public void getBillPayees(final VolleyCallBack callback) {
        params = new HashMap<>();
        params.put(context.getString(R.string.TOKEN), UserDataStore.getDataStore().getToken());
        makeRequest(callback, Request.Method.POST, Variables.BILL_PAYEE);
    }

    public void submitBillPayment(final VolleyCallBack callback, Map<String, Object> params) {
        this.params = params;
        params.put(context.getString(R.string.TOKEN), UserDataStore.getDataStore().getToken());
        makeRequest(callback, Request.Method.POST, Variables.BILL_PAYMENT);
    }

}


