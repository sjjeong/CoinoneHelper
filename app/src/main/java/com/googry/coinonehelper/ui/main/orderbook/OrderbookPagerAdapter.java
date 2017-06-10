package com.googry.coinonehelper.ui.main.orderbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookPagerAdapter extends FragmentStatePagerAdapter {
    private static final int COIN_CNT = 4;

    public OrderbookPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        String coinType = null;
        switch (position){
            case 0:
                coinType = "btc";
                break;
            case 1:
                coinType = "eth";
                break;
            case 2:
                coinType = "etc";
                break;
            case 3:
                coinType = "xrp";
                break;
            default:
                coinType = "btc";
                break;
        }
        return OrderbookFragment.newInstance(coinType);
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
