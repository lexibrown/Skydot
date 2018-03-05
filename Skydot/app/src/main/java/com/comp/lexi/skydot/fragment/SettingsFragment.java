package com.comp.lexi.skydot.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.comp.lexi.skydot.activity.LoginActivity;
import com.comp.lexi.skydot.base.BaseFragment;
import com.comp.lexi.skydot.R;
import com.comp.lexi.skydot.utils.RequestUtil;
import com.comp.lexi.skydot.utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsFragment extends BaseFragment {

    private EditText passwordTxt;
    private EditText confirmPasswordTxt;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inflate the layout for this fragment
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                    getString(R.string.title_settings));
        }

        passwordTxt = (EditText) view.findViewById(R.id.password);
        confirmPasswordTxt = (EditText) view.findViewById(R.id.confirmPassword);

        Button btnDelete = (Button) view.findViewById(R.id.delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptDelete();
            }
        });

        return view;
    }

    public void attemptDelete() {
        // Reset errors.
        passwordTxt.setError(null);
        confirmPasswordTxt.setError(null);

        String password = passwordTxt.getText().toString();
        String rePassword = confirmPasswordTxt.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(rePassword)) {
            confirmPasswordTxt.setError(getString(R.string.error_field_required));
            focusView = confirmPasswordTxt;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordTxt.setError(getString(R.string.error_field_required));
            focusView = passwordTxt;
            cancel = true;
        } else if (!rePassword.equals(password)) {
            passwordTxt.setError(getString(R.string.error_password_mismatch));
            confirmPasswordTxt.setError(getString(R.string.error_password_mismatch));
            focusView = passwordTxt;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            deleteAccount(password);
        }
    }

    public void deleteAccount(String password) {
        showProgress();
        RequestUtil.getInstance(getActivity()).deleteProfile(new VolleyCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                dismissProgress();
                try {
                    if (result.has(getString(R.string.ERROR))) {
                        showError(result.getString(getString(R.string.MESSAGE)), result.getString(getString(R.string.ERROR)));
                    } else {
                        showMessage(result.getString(getString(R.string.MESSAGE)), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            }
                        });
                    }
                } catch (JSONException je) {
                    showError("Json parse failed.");
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
        }, password);
    }

}
