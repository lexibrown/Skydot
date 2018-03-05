package com.comp.lexi.skydot.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comp.lexi.skydot.R;
import com.comp.lexi.skydot.base.BaseActivity;
import com.comp.lexi.skydot.base.BaseTransactionAdapter;
import com.comp.lexi.skydot.data.Account;
import com.comp.lexi.skydot.data.Transaction;
import com.comp.lexi.skydot.utils.JsonUtil;
import com.comp.lexi.skydot.utils.RequestUtil;
import com.comp.lexi.skydot.utils.Variables;
import com.comp.lexi.skydot.utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountDetailsActivity extends BaseActivity {

    private TextView txtName;
    private TextView txtCAD;
    private TextView txtUSD;
    private TextView txtId;

    private ListView historyListView;
    private BaseTransactionAdapter historyAdapter;

    private int accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtName = (TextView) findViewById(R.id.name);
        txtCAD = (TextView) findViewById(R.id.cad);
        txtUSD = (TextView) findViewById(R.id.usd);
        txtId = (TextView) findViewById(R.id.id);

        historyListView = (ListView) findViewById(R.id.history_list);
        historyAdapter = new BaseTransactionAdapter(this, new ArrayList<Transaction>());
        historyListView.setAdapter(historyAdapter);
        BaseTransactionAdapter.setListViewHeightBasedOnChildren(historyListView);

        accountID = getIntent().getIntExtra(Variables.ACCOUNTID, 0);
        if (accountID == 0) {
            goBack();
        }
        getAccountInfo(accountID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void displayAccount(Account account) {
        txtName.setText(account.getName());

        if (account.getBalanceCAD() == 0.0) {
            txtCAD.setVisibility(View.GONE);
        } else {
            txtCAD.setText(String.format(getString(R.string.balance_cad), account.getBalanceCAD()));
            txtCAD.setVisibility(View.VISIBLE);
        }
        if (account.getBalanceUSD() == 0.0) {
            txtUSD.setVisibility(View.GONE);
        } else {
            txtUSD.setText(String.format(getString(R.string.balance_usd), account.getBalanceUSD()));
            txtUSD.setVisibility(View.VISIBLE);
        }

        txtId.setText("*" + account.getId());

        historyAdapter.updateList(account.getHistory());
        BaseTransactionAdapter.setListViewHeightBasedOnChildren(historyListView);

    }

    private void getAccountInfo(int id) {
        showProgress();
        RequestUtil.getInstance(this).getAccountDetails(new VolleyCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                dismissProgress();
                if (result.has(getString(R.string.ERROR))) {
                    try {
                        showError(result.getString(getString(R.string.MESSAGE)), result.getString(getString(R.string.ERROR)));
                    } catch (JSONException je) {
                        showError("Json parse failed.");
                    }
                } else {
                    try {
                        Account account = JsonUtil.parse(result.toString(), Account.class);
                        displayAccount(account);
                    } catch (IOException ioe) {
                        showError("Event parse failed.");
                        ioe.printStackTrace();
                    }
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
        }, id);
    }

}
