package com.googry.coinonehelper.ui.main.coin_volume.coin_volume_detail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinMarket;
import com.googry.coinonehelper.data.CoinMarketCap;
import com.googry.coinonehelper.databinding.CoinVolumeDetailFragmentBinding;
import com.googry.coinonehelper.util.NumberConvertUtil;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 10. 17..
 */

public class CoinVolumeDetailFragment extends BaseFragment<CoinVolumeDetailFragmentBinding> implements CoinVolumeDetailContract.View {
    public static final String KEY_COIN_TYPE = "KEY_COIN_MARKET_CAP";

    private CoinVolumeDetailContract.Presenter mPresenter;

    private CoinVolumeDetailAdapter mCoinVolumeDetailAdapter;

    public static CoinVolumeDetailFragment newInstance(CoinMarketCap coinMarketCap) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_COIN_TYPE, coinMarketCap);

        CoinVolumeDetailFragment fragment = new CoinVolumeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(CoinVolumeDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.coin_volume_detail_fragment;
    }

    @Override
    protected void initView() {
        mCoinVolumeDetailAdapter = new CoinVolumeDetailAdapter();
        mBinding.rvMarkets.setAdapter(mCoinVolumeDetailAdapter);
    }

    @Override
    protected void newPresenter() {
        CoinMarketCap coinMarketCap = getArguments().getParcelable(KEY_COIN_TYPE);
        new CoinVolumeDetailPresenter(this, coinMarketCap);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }

    @Override
    public void refresh() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCoinVolumeDetailAdapter.notifyDataSetChanged();
                    mBinding.progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    public void setData(ArrayList<CoinMarket> coinMarkets) {
        mCoinVolumeDetailAdapter.setCoinMarkets(coinMarkets);
    }

    @Override
    public void showPieChart(final ArrayList<CoinMarket> coinMarkets, final String coinName) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    int lastPosition = coinMarkets.size() >= 10 ? 10 : coinMarkets.size();
                    for (int i = 0; i < lastPosition; i++) {
                        CoinMarket coinMarket = coinMarkets.get(i);
                        int volume24 = NumberConvertUtil.convertDollarStringToInteger(coinMarket.volume24);
                        pieEntries.add(new PieEntry(volume24, coinMarket.source));
                    }
                    int volume24 = 0;
                    for (int i = lastPosition; i < coinMarkets.size(); i++) {
                        CoinMarket coinMarket = coinMarkets.get(i);
                        volume24 += NumberConvertUtil.convertDollarStringToInteger(coinMarket.volume24);
                    }
                    pieEntries.add(new PieEntry(volume24, getString(R.string.others)));

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, coinName);

                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for (int c : ColorTemplate.VORDIPLOM_COLORS)
                        colors.add(c);

                    for (int c : ColorTemplate.JOYFUL_COLORS)
                        colors.add(c);

                    for (int c : ColorTemplate.COLORFUL_COLORS)
                        colors.add(c);

                    for (int c : ColorTemplate.LIBERTY_COLORS)
                        colors.add(c);

                    for (int c : ColorTemplate.PASTEL_COLORS)
                        colors.add(c);

                    colors.add(ColorTemplate.getHoloBlue());

                    pieDataSet.setColors(colors);

                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueFormatter(new PercentFormatter());
                    pieData.setValueTextSize(11f);
                    pieData.setValueTextColor(Color.BLACK);
                    mBinding.picChartMarkets.setEntryLabelColor(Color.BLACK);
                    mBinding.picChartMarkets.getLegend().setEnabled(false);
                    mBinding.picChartMarkets.getDescription().setEnabled(false);
                    mBinding.picChartMarkets.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                    mBinding.picChartMarkets.setCenterText(coinName);
                    mBinding.picChartMarkets.setCenterTextSize(28);
                    mBinding.picChartMarkets.setData(pieData);
                    mBinding.picChartMarkets.setUsePercentValues(true);
                    mBinding.picChartMarkets.invalidate();
                }
            });

        }
    }

}
