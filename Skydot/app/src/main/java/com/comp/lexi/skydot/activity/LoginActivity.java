package com.comp.lexi.skydot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.comp.lexi.skydot.R;
import com.comp.lexi.skydot.base.BaseActivity;
import com.comp.lexi.skydot.database.UserDataStore;
import com.comp.lexi.skydot.utils.RequestUtil;
import com.comp.lexi.skydot.utils.Variables;
import com.comp.lexi.skydot.utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    private EditText usernameView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserDataStore dataStore = UserDataStore.getDataStore();
        dataStore.loadState(getApplicationContext());

        // Set up the login form.
        usernameView = (EditText) findViewById(R.id.user);
        if (dataStore.isThereAUser()) {
            usernameView.setText(dataStore.getUserid());
        }

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView registerLink = (TextView) findViewById(R.id.link_register);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_center_slide,
                        R.anim.center_to_left_slide);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(false);
    }

    private void attemptLogin() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ?
                        null : getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Reset errors.
        usernameView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameView.getText().toString();
        final String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            usernameView.setError(getString(R.string.error_invalid_username));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            updateToNewUser(username);
            showProgress();
            RequestUtil.getInstance(this).login(new VolleyCallBack() {
                @Override
                public void onSuccess(JSONObject result) {
                    usernameView.clearFocus();
                    passwordView.clearFocus();
                    dismissProgress();

                    if (result.has(getString(R.string.TOKEN))) {
                        try {
                            UserDataStore.getDataStore().setToken(result.getString(getString(R.string.TOKEN)));
                            goToMenu();
                        } catch (JSONException je) {
                            showError("Json parse failed.");
                        }
                    } else if (result.has(getString(R.string.ERROR))) {
                        try {
                            showError(result.getString(getString(R.string.MESSAGE)), result.getString(getString(R.string.ERROR)));
                        } catch (JSONException je) {
                            showError("Json parse failed.");
                        }
                    } else {
                        showError(result.toString());
                    }
                }

                @Override
                public void onSuccess(JSONArray result) {
                    // do nothing
                }

                @Override
                public void onSuccess(String result) {
                    // do nothing
                }

                @Override
                public void onFailure(String result) {
                    dismissProgress();
                    showError(result);
                }
            }, username, password);
        }
    }

    private boolean isUsernameValid(String username) {
        return username.length() == Variables.USERNAME_LENGTH;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= Variables.MIN_PASSWORD_LENGTH
                && password.length() <= Variables.MAX_PASSWORD_LENGTH;
    }

    private void goToMenu() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void updateToNewUser(String user) {
        UserDataStore.getDataStore().setUserid(user);
    }

}