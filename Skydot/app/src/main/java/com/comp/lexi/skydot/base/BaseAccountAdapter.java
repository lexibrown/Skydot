package com.comp.lexi.skydot.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comp.lexi.skydot.R;
import com.comp.lexi.skydot.data.AccountListItem;

import java.util.List;

public class BaseAccountAdapter extends ArrayAdapter<AccountListItem> {

    private int resource;
    private LayoutInflater mLayoutInflater;

    public BaseAccountAdapter(Context context, List<AccountListItem> objects) {
        super(context, R.layout.account_list_item, objects);
        resource = R.layout.account_list_item;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateList(List<AccountListItem> accounts) {
        clear();
        addAll(accounts);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        convertView = mLayoutInflater.inflate(resource, null);

        AccountListItem item = getItem(position);

        TextView txtName = (TextView) convertView.findViewById(R.id.name);
        txtName.setText(item.getName());

        TextView txtCAD = (TextView) convertView.findViewById(R.id.cad);
        if (item.getBalanceCAD() == 0.0) {
            txtCAD.setVisibility(View.GONE);
        } else {
            txtCAD.setText(String.format(getContext().getString(R.string.balance_cad), item.getBalanceCAD()));
            txtCAD.setVisibility(View.VISIBLE);
        }

        TextView txtUSD = (TextView) convertView.findViewById(R.id.usd);
        if (item.getBalanceUSD() == 0.0) {
            txtUSD.setVisibility(View.GONE);
        } else {
            txtUSD.setText(String.format(getContext().getString(R.string.balance_usd), item.getBalanceUSD()));
            txtUSD.setVisibility(View.VISIBLE);
        }

        TextView txtId = (TextView) convertView.findViewById(R.id.id);
        txtId.setText("*" + item.getId());

        return convertView;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ArrayAdapter listAdapter = (ArrayAdapter<AccountListItem>) listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
