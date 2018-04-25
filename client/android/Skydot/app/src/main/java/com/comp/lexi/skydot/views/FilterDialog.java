package com.comp.lexi.skydot.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.comp.lexi.skydot.R;

import java.util.Map;

public class FilterDialog extends Dialog {

    private static DialogListener listener;

    public void setDialogListener(DialogListener l) {
        listener = l;
    }

    public FilterDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_filter_dialog);



        // TODO
//        public void onClick(DialogInterface dialog, int id) {
//            for (DialogListener listener : listeners) {
//                // TODO
//                listener.onSearch(null);
//            }
//        }

    }

    public interface DialogListener {
        public void onSearch(Map<String, Object> params);
    }

}