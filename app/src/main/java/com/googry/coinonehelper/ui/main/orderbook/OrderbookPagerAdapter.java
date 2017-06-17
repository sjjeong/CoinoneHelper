package com.googry.coinonehelper.ui.main.orderbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.googry.coinonehelper.data.CoinType;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookPagerAdapter extends FragmentStatePagerAdapter {
    private static final int COIN_CNT = CoinType.values().length;
    private OrderbookFragment[] mOrderbookFragments;
    private String[] mCoinTypes = {"btc", "eth", "etc", "xrp"};

    public OrderbookPagerAdapter(FragmentManager fm) {
        super(fm);
        mOrderbookFragments = new OrderbookFragment[COIN_CNT];
        for (int i = 0; i < COIN_CNT; i++) {
            mOrderbookFragments[i] = OrderbookFragment.newInstance(mCoinTypes[i]);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mOrderbookFragments[position];
    }

    @Override
    public int getCount() {
        return COIN_CNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }


}
