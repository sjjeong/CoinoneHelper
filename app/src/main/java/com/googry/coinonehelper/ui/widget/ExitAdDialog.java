package com.googry.coinonehelper.ui.widget;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.databinding.ExitDialogBinding;

/**
 * Created by seokjunjeong on 2017. 6. 18..
 */

public class ExitAdDialog extends DialogFragment {
    private ExitDialogBinding mBinding;
    private NativeExpressAdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exit_dialog, container);
        mBinding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setDialog(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdView = mBinding.adView;


//        MobileAds.initialize(getContext(), getString(R.string.admob_app_id));
        final AdRequest request = new AdRequest.Builder().build();
        mAdView.loadAd(request);
        mAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        mAdView.pause();

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        // Destroy the AdView.
        mAdView.destroy();
        super.onDestroyView();
    }

    public void onExitClick(View v) {
        getActivity().finish();
    }

    public void onCancelClick(View v) {
        dismiss();
    }


}
