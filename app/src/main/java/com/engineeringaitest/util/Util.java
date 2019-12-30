package com.engineeringaitest.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.engineeringaitest.R;

public class Util {
    private static Dialog dialog;

    //print Exception with exception value
    public static void printException(String value, Throwable e) {
        Log.e("!_@_TestExam:", value + "", e);
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showProgress(Context context, boolean cancellable) {
        if (context == null)
            return;

        if (checkProgressOpen())
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

    private static boolean checkProgressOpen() {
        return dialog != null && dialog.isShowing();
    }

    public static void cancelProgress() {
        if (checkProgressOpen()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                printException("Exception ", e);
            }
            dialog = null;
        }
    }
}