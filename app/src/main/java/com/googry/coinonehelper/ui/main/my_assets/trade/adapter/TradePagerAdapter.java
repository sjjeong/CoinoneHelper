package com.googry.coinonehelper.ui.main.my_assets.trade.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.googry.coinonehelper.ui.main.my_assets.trade.AskBidFragment;
import com.googry.coinonehelper.ui.main.my_assets.trade.AskBidViewModel;
import com.googry.coinonehelper.ui.main.my_assets.trade.ConclusionHistoryFragment;
import com.googry.coinonehelper.ui.main.my_assets.trade.ConclusionHistoryViewModel;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class TradePagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] mFragments;
    private String[] mTitles;

    public TradePagerAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mFragments = new Fragment[3];
        mFragments[0] = AskBidFragment.newInstance(false);
        mFragments[1] = AskBidFragment.newInstance(true);
        mFragments[2] = ConclusionHistoryFragment.newInstance();
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    public void setAskBidViewModel(AskBidViewModel askBidViewModel) {
        ((AskBidFragment) mFragments[0]).setAskBidViewModel(askBidViewModel);
        ((AskBidFragment) mFragments[1]).setAskBidViewModel(askBidViewModel);
    }

    public void setConclusionHistoryViewModel(ConclusionHistoryViewModel conclusionHistoryViewModel) {
        ((ConclusionHistoryFragment) mFragments[2]).setConclusionHistoryViewModel(conclusionHistoryViewModel);
    }
}
