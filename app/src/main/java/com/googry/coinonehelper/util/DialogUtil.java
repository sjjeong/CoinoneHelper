package com.googry.coinonehelper.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by seokjunjeong on 2017. 5. 30..
 */

public class DialogUtil {
    private static ProgressDialog mProgressDialog;

    public static void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
        }
        mProgressDialog.dismiss();
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
