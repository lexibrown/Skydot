package com.comp.lexi.skydot.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VolleyCallBack {
    void onSuccess(String result);
    void onSuccess(JSONObject result);
    void onSuccess(JSONArray result);

    void onFailure(String result);
}

