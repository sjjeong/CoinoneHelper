package com.googry.coinonehelper.korbit.ui.main.orderbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.googry.coinonehelper.korbit.data.KorbitCoinType;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class KorbitOrderbookPagerAdapter extends FragmentStatePagerAdapter {
    private static final int COIN_CNT = KorbitCoinType.values().length;
    private KorbitOrderbookFragment[] mKorbitOrderbookFragments;

    public KorbitOrderbookPagerAdapter(FragmentManager fm) {
        super(fm);
        mKorbitOrderbookFragments = new KorbitOrderbookFragment[COIN_CNT];
        for (int i = 0; i < COIN_CNT; i++) {
            mKorbitOrderbookFragments[i] = KorbitOrderbookFragment.newInstance(KorbitCoinType.values()[i]);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mKorbitOrderbookFragments[position];
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
