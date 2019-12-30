package com.engineeringaitest.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.engineeringaitest.R;

public class Utils {
    private static Dialog dialog;

    public static void printException(String value, Throwable e) {
        Log.e("!_@_TestExam:", value + "", e);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void startProgress(Context context, boolean cancellable) {
        if (context == null)
            return;

        if (isLoading())
            return;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(cancellable);
        try {
            dialog.show();
        } catch (Exception e) {
            printException("Exception ", e);
        }
    }

    private static boolean isLoading() {
        return dialog != null && dialog.isShowing();
    }

    public static void dismissProgress() {
        if (isLoading()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                printException("Exception ", e);
            }
            dialog = null;
        }
    }
}