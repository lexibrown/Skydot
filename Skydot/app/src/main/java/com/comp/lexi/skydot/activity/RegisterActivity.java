package com.comp.lexi.skydot.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.comp.lexi.skydot.base.BaseActivity;
import com.comp.lexi.skydot.R;
import com.comp.lexi.skydot.utils.RequestUtil;
import com.comp.lexi.skydot.utils.Variables;
import com.comp.lexi.skydot.utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity {

    private EditText usernameView;
    private EditText passwordView;
    private EditText rePasswordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameView = (EditText) findViewById(R.id.input_username);
        passwordView = (EditText) findViewById(R.id.input_password);
        rePasswordView = (EditText) findViewById(R.id.input_reEnterPassword);

        Button signUpButton = (Button) findViewById(R.id.btn_signup);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });

        TextView loginLink = (TextView) findViewById(R.id.link_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void attemptRegistration() {
        // Reset errors.
        usernameView.setError(null);
        passwordView.setError(null);
        rePasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String rePassword = rePasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            String message = String.format(getString(R.string.error_invalid_username_length),
                    Variables.USERNAME_LENGTH);
            usernameView.setError(message);
            focusView = usernameView;
            cancel = true;
        } else if (!allNumbers(username)) {
            usernameView.setError(getString(R.string.error_invalid_username_contains));
            focusView = usernameView;
            cancel = true;
        }

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

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(rePassword)) {
            rePasswordView.setError(getString(R.string.error_field_required));
            focusView = rePasswordView;
            cancel = true;
        } else if (!isPasswordValid(rePassword)) {
            rePasswordView.setError(getString(R.string.error_invalid_password));
            focusView = rePasswordView;
            cancel = true;
        } else if (!rePassword.equals(password)) {
            passwordView.setError(getString(R.string.error_password_mismatch));
            rePasswordView.setError(getString(R.string.error_password_mismatch));
            focusView = passwordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress();
            RequestUtil.getInstance(this).register(new VolleyCallBack() {
                @Override
                public void onSuccess(JSONObject result) {
                    dismissProgress();
                    if (result.has(getString(R.string.TOKEN))) {
                        try {
                            showMessage(result.getString(getString(R.string.MESSAGE)));
                        } catch (JSONException je) {
                            showMessage("Profile created.");
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

    private boolean allNumbers(String username) {
        return username.matches("[0-9]+");
    }

    private boolean isUsernameValid(String username) {
        return username.length() == Variables.USERNAME_LENGTH;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= Variables.MIN_PASSWORD_LENGTH
                && password.length() <= Variables.MAX_PASSWORD_LENGTH;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        finish();
        overridePendingTransition(R.anim.left_to_center_slide,
                R.anim.center_to_right_slide);
    }

}