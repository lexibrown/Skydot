package com.comp.lexi.skydot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.comp.lexi.skydot.R;
import com.comp.lexi.skydot.activity.AccountDetailsActivity;
import com.comp.lexi.skydot.base.BaseAccountAdapter;
import com.comp.lexi.skydot.base.BaseFragment;
import com.comp.lexi.skydot.data.AccountListItem;
import com.comp.lexi.skydot.utils.JsonUtil;
import com.comp.lexi.skydot.utils.RequestUtil;
import com.comp.lexi.skydot.utils.Variables;
import com.comp.lexi.skydot.utils.VolleyCallBack;
import com.comp.lexi.skydot.views.PullScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends BaseFragment implements PullScrollView.OnTurnListener {

    private LinearLayout noAccounts;

    private ListView bankingListView;
    private ListView borrowingListView;
    private ListView investingListView;

    private BaseAccountAdapter bankingAdapter;
    private BaseAccountAdapter borrowingAdapter;
    private BaseAccountAdapter investingAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                    getString(R.string.title_profile));
        }

        noAccounts = (LinearLayout) view.findViewById(R.id.no_accounts);
        noAccounts.setVisibility(View.GONE);

        bankingListView = (ListView) view.findViewById(R.id.banking_account_list);
        bankingAdapter = new BaseAccountAdapter(getActivity(), new ArrayList<AccountListItem>());
        bankingListView.setAdapter(bankingAdapter);
        bankingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountListItem account = bankingAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), AccountDetailsActivity.class);
                intent.putExtra(Variables.ACCOUNTID, account.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        BaseAccountAdapter.setListViewHeightBasedOnChildren(bankingListView);

        borrowingListView = (ListView) view.findViewById(R.id.borrowing_account_list);
        borrowingAdapter = new BaseAccountAdapter(getContext(), new ArrayList<AccountListItem>());
        borrowingListView.setAdapter(borrowingAdapter);
        borrowingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountListItem event = borrowingAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), AccountDetailsActivity.class);
                intent.putExtra(Variables.ACCOUNTID, event.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        BaseAccountAdapter.setListViewHeightBasedOnChildren(borrowingListView);

        investingListView = (ListView) view.findViewById(R.id.investing_account_list);
        investingAdapter = new BaseAccountAdapter(getContext(), new ArrayList<AccountListItem>());
        investingListView.setAdapter(investingAdapter);
        investingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountListItem event = investingAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), AccountDetailsActivity.class);
                intent.putExtra(Variables.ACCOUNTID, event.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        BaseAccountAdapter.setListViewHeightBasedOnChildren(investingListView);

        getAccountSummary();
        return view;
    }

    @Override
    public void onTurn() {
        getAccountSummary();
    }

    private void getAccountSummary() {
        showProgress();
        RequestUtil.getInstance(getActivity()).getAccountSummary(new VolleyCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                dismissProgress();

                if (result.has(getString(R.string.ACCOUNTS))) {
                    try {
                        String str = result.getString(getString(R.string.ACCOUNTS));
                        List<AccountListItem> accounts = new ArrayList<>();
                        if (str != null && !str.isEmpty() && !"null".equals(str)) {
                            accounts = Arrays.asList(JsonUtil.parse(
                                    str, AccountListItem[].class));
                        }

                        if (accounts.isEmpty()) {
                            bankingListView.setVisibility(View.GONE);
                            borrowingListView.setVisibility(View.GONE);
                            investingListView.setVisibility(View.GONE);
                            noAccounts.setVisibility(View.VISIBLE);
                            return;
                        }
                        sortList(accounts);
                    } catch (IOException ioe) {
                        showError("Account list parse failed.");
                    } catch (Exception e) {
                        showError("Unknown issue.");
                        e.printStackTrace();
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
                bankingListView.setVisibility(View.GONE);
                borrowingListView.setVisibility(View.GONE);
                investingListView.setVisibility(View.GONE);
                noAccounts.setVisibility(View.VISIBLE);
                showError(result);
            }
        });
    }

    private void sortList(List<AccountListItem> accounts) {
        List<AccountListItem> banking = new ArrayList<>();
        List<AccountListItem> borrowing = new ArrayList<>();
        List<AccountListItem> investing = new ArrayList<>();

        for (int i = 0; i < accounts.size(); i++) {
            AccountListItem account = accounts.get(i);
            if (account.getType().equals(Variables.AccountType.BANKING.getValue())) {
                banking.add(account);
            } else if (account.getType().equals(Variables.AccountType.BORROWING.getValue())) {
                borrowing.add(account);
            } else if (account.getType().equals(Variables.AccountType.INVESTING.getValue())) {
                investing.add(account);
            }
        }

        bankingAdapter.updateList(banking);
        BaseAccountAdapter.setListViewHeightBasedOnChildren(bankingListView);
        borrowingAdapter.updateList(borrowing);
        BaseAccountAdapter.setListViewHeightBasedOnChildren(borrowingListView);
        investingAdapter.updateList(investing);
        BaseAccountAdapter.setListViewHeightBasedOnChildren(investingListView);
    }

}