package com.comp.lexi.skydot.database;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDataStore {

    private static final String EMPTY_STRING = "";
    private static UserDataStore dataStore;
    private static final String PREFERENCES = "PREFERENCES";
    private static final String USER_KEY = "USER_KEY";

    private String userid = null;
    private String token = null;

    static {
        if (dataStore == null)
            dataStore = new UserDataStore();
    }

    private UserDataStore() {
    }

    public static UserDataStore getDataStore() {
        return dataStore;
    }

    public boolean saveState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (getUser() == null) {
            editor.putString(USER_KEY, EMPTY_STRING);
        } else {
            try {
                editor.putString(USER_KEY, getUser());
            } catch (Exception e) {
                editor.putString(USER_KEY, EMPTY_STRING);
            }
        }
        return editor.commit();
    }

    public boolean clearState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        setUser(null);
        editor.remove(USER_KEY);
        return editor.commit();
    }

    public void loadState(Context context) {
        String user;
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        user = sharedPreference.getString(USER_KEY, null);
        if ((user == null) || (user.isEmpty())) {
            setUser(null);
        } else {
            setUser(user);
        }
    }

    public String getUser() {
        return this.userid;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUser(String user) {
        this.userid = user;
    }

    public void updateProfile(String user) {
        this.userid = user;
    }

    public void clearUser() {
        setUser(null);
    }

    public boolean isThereAUser() {
        return getUser() != null;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

}