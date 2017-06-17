package com.googry.coinonehelper.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import com.googry.coinonehelper.R;

/**
 * Created by seokjunjeong on 2017. 5. 30..
 */

public class DialogUtil {
    private static ProgressDialog mServerDownProgressDialog;

    private static void createServerDownProgressDialog(Context context) {
        mServerDownProgressDialog = new ProgressDialog(context);
        mServerDownProgressDialog.setMessage(context.getString(R.string.coinone_server_down));
    }

    public static void showServerDownProgressDialog(Context context) {
        if (mServerDownProgressDialog == null && context != null) {
            createServerDownProgressDialog(context);
        }
        if (mServerDownProgressDialog.isShowing()) return;

        try {
            mServerDownProgressDialog.show();
        } catch (WindowManager.BadTokenException e) {
            mServerDownProgressDialog = null;
            e.printStackTrace();
        }
    }

    public static void hideProgressDialog() {
        if (mServerDownProgressDialog != null) {
            mServerDownProgressDialog.dismiss();
        }
    }
}
