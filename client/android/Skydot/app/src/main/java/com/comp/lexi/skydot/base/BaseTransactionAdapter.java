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
import com.comp.lexi.skydot.data.Transaction;

import java.util.List;

public class BaseTransactionAdapter extends ArrayAdapter<Transaction> {

    private int resource;
    private LayoutInflater mLayoutInflater;

    public BaseTransactionAdapter(Context context, List<Transaction> objects) {
        super(context, R.layout.transaction_list_item, objects);
        resource = R.layout.transaction_list_item;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateList(List<Transaction> transactions) {
        clear();
        addAll(transactions);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        convertView = mLayoutInflater.inflate(resource, null);

        Transaction item = getItem(position);

        TextView txtName = (TextView) convertView.findViewById(R.id.name);
        txtName.setText(item.getName());

        TextView txtAmount = (TextView) convertView.findViewById(R.id.amount);
        if (item.getAmount() < 0.0) {
            txtAmount.setTextColor(getContext().getResources().getColor(R.color.red));
        } else {
            txtAmount.setTextColor(getContext().getResources().getColor(R.color.black));
        }
        txtAmount.setText(String.format(getContext().getString(R.string.amount), item.getAmount()));

        TextView txtId = (TextView) convertView.findViewById(R.id.date);
        txtId.setText(item.getDate());

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
