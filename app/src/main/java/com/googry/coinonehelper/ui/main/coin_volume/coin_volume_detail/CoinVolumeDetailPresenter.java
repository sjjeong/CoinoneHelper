package com.googry.coinonehelper.ui.main.coin_volume.coin_volume_detail;

import android.os.AsyncTask;

import com.googry.coinonehelper.data.CoinMarket;
import com.googry.coinonehelper.data.CoinMarketCap;
import com.googry.coinonehelper.util.NumberConvertUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 10. 17..
 */

public class CoinVolumeDetailPresenter implements CoinVolumeDetailContract.Presenter {
    private final String BASE_URL = "https://coinmarketcap.com";
    private CoinVolumeDetailContract.View mView;

    private ArrayList<CoinMarket> mCoinMarkets;

    private CoinMarketCap mCoinMarketCap;

    public CoinVolumeDetailPresenter(CoinVolumeDetailContract.View view,
                                     CoinMarketCap coinMarketCap) {
        mView = view;
        mView.setPresenter(this);

        mCoinMarketCap = coinMarketCap;
        mCoinMarkets = new ArrayList<>();
    }

    @Override
    public void start() {
        mView.setData(mCoinMarkets);
        crawlingCoinList();
    }

    private void crawlingCoinList() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mCoinMarkets.clear();

                    Document doc = Jsoup.connect(BASE_URL + mCoinMarketCap.marketsUrl).get();
                    Elements tables = doc.select(".table-responsive tbody tr");

                    for (Element table : tables) {
                        Elements elements = table.getElementsByTag("td");
                        CoinMarket coinMarket = new CoinMarket();
                        coinMarket.source = elements.get(1).text();
                        coinMarket.pair = elements.get(2).text();
                        coinMarket.volume24 = elements.get(3).text();
                        coinMarket.price = NumberConvertUtil.convertDollarStringToDouble(elements.get(4).text());
                        coinMarket.volumePercent = elements.get(5).text();
                        if (!coinMarket.volumePercent.equals("0.00%")) {
                            mCoinMarkets.add(coinMarket);
                        }
                    }
                    mView.refresh();
                    mView.showPieChart(mCoinMarkets, mCoinMarketCap.name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void loadMarkets() {
        crawlingCoinList();
    }
}
