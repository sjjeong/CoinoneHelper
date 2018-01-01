package com.googry.coinonehelper.ui.main.my_assets;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.googry.coinonehelper.Injection;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneBalance;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.MarketAccount;
import com.googry.coinonehelper.databinding.MyAssetsFragmentBinding;
import com.googry.coinonehelper.ui.main.my_assets.adapter.MyAssetsAdapter;
import com.googry.coinonehelper.ui.setting.SettingActivity;
import com.googry.coinonehelper.util.PrefUtil;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 10. 28..
 */

public class MyAssetsFragment extends BaseFragment<MyAssetsFragmentBinding> implements MyAssetsContract.View {
    private MyAssetsContract.Presenter mPresenter;
    private MyAssetsAdapter mMyAssetsAdapter;

    private ProgressDialog mProgressDialog;

    private ArrayList<Integer> mColorRes;

    private Realm mRealm;

    public static MyAssetsFragment newInstance() {

        Bundle args = new Bundle();

        MyAssetsFragment fragment = new MyAssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(MyAssetsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_assets_fragment;
    }

    @Override
    protected void initView() {
        mMyAssetsAdapter = new MyAssetsAdapter();
        mBinding.rvMyAssets.setAdapter(mMyAssetsAdapter);
        mColorRes = new ArrayList<>();
        mColorRes.add(R.color.colorBtc);
        mColorRes.add(R.color.colorBch);
        mColorRes.add(R.color.colorEth);
        mColorRes.add(R.color.colorEtc);
        mColorRes.add(R.color.colorXrp);
        mColorRes.add(R.color.colorQtum);
        mColorRes.add(R.color.colorLtc);
        mColorRes.add(R.color.colorIota);
        mColorRes.add(R.color.colorBtg);
        mColorRes.add(R.color.colorKrw);


        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.checkRegisterAccount();
            }
        });
    }

    @Override
    protected void newPresenter() {
        mRealm = Injection.getSecureRealm();
        new MyAssetsPresenter(this, getContext(), mRealm);
    }

    @Override
    public void onDestroyView() {
        mRealm.close();
        super.onDestroyView();
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.checkRegisterAccount();
    }

    @Override
    public void showSettingUi() {
        mMyAssetsAdapter.setAccessible(false);
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.ask_register_account)
                .setPositiveButton(R.string.move, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), SettingActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        hideLoadingDialog();
                    }
                })
                .show();
    }

    @Override
    public void showBalance(final CoinoneBalance balance) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CoinType[] coinTypes = CoinType.values();
                    ArrayList<CoinoneBalance.Balance> balances = new ArrayList<>();
                    balances.add(balance.balanceBtc);
                    balances.add(balance.balanceBch);
                    balances.add(balance.balanceEth);
                    balances.add(balance.balanceEtc);
                    balances.add(balance.balanceXrp);
                    balances.add(balance.balanceQtum);
                    balances.add(balance.balanceLtc);
                    balances.add(balance.balanceIota);
                    balances.add(balance.balanceBtg);
                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    Gson gson = new Gson();

                    ArrayList<LegendEntry> legendEntries = new ArrayList<>();

                    ArrayList<MyAssetsAdapter.MyAssetsItem> myAssetsItems = new ArrayList<>();

                    ArrayList<Integer> colors = new ArrayList<>();

                    float krw = 0;
                    float value;
                    String label;
                    for (int i = 0; i < coinTypes.length; i++) {
                        CoinType _coinType = coinTypes[i];
                        CoinoneBalance.Balance _balance = balances.get(i);
                        if (_balance == null) {
                            continue;
                        }
                        CoinoneTicker.Ticker ticker = gson.fromJson(PrefUtil.loadTicker(getContext(), _coinType), CoinoneTicker.Ticker.class);
                        value = (float) (_balance.balance * ticker.last);
                        label = _coinType.name();
                        pieEntries.add(new PieEntry(value, label));
                        LegendEntry legendEntry = new LegendEntry();
                        legendEntry.label = String.format("%s: %,d", label, (int) value);
                        legendEntries.add(legendEntry);
                        myAssetsItems.add(new MyAssetsAdapter.MyAssetsItem(label, _balance.avail, _balance.balance, (long) value,
                                ResourcesCompat.getColor(getResources(), mColorRes.get(i), null)));
                        colors.add(ResourcesCompat.getColor(getResources(), mColorRes.get(i), null));

                        krw += value;
                    }
                    value = (float) (balance.balanceKrw.balance);
                    label = "KRW";
                    pieEntries.add(new PieEntry(value, label));
                    krw += value;
                    LegendEntry legendEntry = new LegendEntry();
                    legendEntry.label = String.format("%s: %,d", label, (int) value);
                    legendEntries.add(legendEntry);
                    myAssetsItems.add(new MyAssetsAdapter.MyAssetsItem(label, balance.balanceKrw.avail, balance.balanceKrw.balance, (long) value,
                            ResourcesCompat.getColor(getResources(), R.color.colorKrw, null)));
                    colors.add(ResourcesCompat.getColor(getResources(), R.color.colorKrw, null));

                    mMyAssetsAdapter.setMyAssetsItems(myAssetsItems);
                    mMyAssetsAdapter.notifyDataSetChanged();

                    String myAssets = String.format("내 자산(KRW)\n%,d", (int) krw);

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

                    pieDataSet.setColors(colors);
                    for (int i = 0; i < legendEntries.size(); i++) {
                        legendEntries.get(i).formColor = colors.get(i);
                    }

                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueFormatter(new PercentFormatter());
                    pieData.setValueTextSize(11f);
                    pieData.setValueTextColor(Color.WHITE);

                    PieChart pieChart = mBinding.picChartMyAssets;
                    pieChart.setEntryLabelColor(Color.WHITE);
                    Legend l = pieChart.getLegend();
                    l.setEnabled(true);
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setDrawInside(false);
                    l.setYEntrySpace(5f);
                    l.setYOffset(-100f);
                    l.setXOffset(10f);
                    l.setCustom(legendEntries);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                    pieChart.setCenterText(myAssets);
                    pieChart.setCenterTextSize(12);
                    pieChart.setData(pieData);
                    pieChart.setUsePercentValues(true);
                    pieChart.invalidate();

                }
            });
        }
    }

    @Override
    public void showLoadingDialog() {
        mMyAssetsAdapter.setAccessible(true);
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.description_load_my_assets_to_coinone));
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (mBinding.swipeRefreshLayout.isRefreshing()) {
            mBinding.swipeRefreshLayout.setRefreshing(false);
        }
    }
}
