package com.googry.coinonehelper.ui.main;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;
import com.googry.coinonehelper.ui.widget.ExitAdDialog;

public class MainActivity extends BaseActivity<MainFragment> {
    @Override
    protected int getLayoutId() {
        return R.layout.base_act;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.content_frame;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected MainFragment getFragment() {
        return MainFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        ExitAdDialog exitAdDialog = new ExitAdDialog();
        exitAdDialog.setCancelable(false);
        exitAdDialog.show(getSupportFragmentManager(),exitAdDialog.getTag());

        //super.onBackPressed();
    }
}
