package com.comp.lexi.skydot.base;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.Toast;

import com.comp.lexi.skydot.R;

import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;
    private final AtomicLong loading = new AtomicLong();

    protected void showProgress() {
        loading.incrementAndGet();
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    protected void dismissProgress() {
        if (loading.get() != 0) {
            loading.decrementAndGet();
        }
        if (progressDialog != null && loading.get() == 0) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    protected void showShortToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showAlert(String msg) {
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_Dark_Dialog))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    protected void showMessage(String msg) {
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_Dark_Dialog))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    protected void showMessage(String msg, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_Dark_Dialog))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.ok, onClickListener).create().show();
    }

    protected void showError(String message) {
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_Dark_Dialog))
                .setTitle(getString(R.string.error_title))
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    protected void showError(String message, String code) {
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_Dark_Dialog))
                .setTitle(getString(R.string.error_title))
                .setMessage(message + " (" + code + ")")
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
