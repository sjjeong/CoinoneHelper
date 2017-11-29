package com.googry.coinonehelper.ui.main.coin_volume;

import android.os.AsyncTask;

import com.googry.coinonehelper.data.CoinMarketCap;
import com.googry.coinonehelper.data.CoinType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 10. 9..
 */

public class CoinVolumePresenter implements CoinVolumeContract.Presenter {
    private final static String BASE_URL = "https://coinmarketcap.com/";

    private CoinVolumeContract.View mView;

    private ArrayList<CoinMarketCap> mCoinMarketCaps;
    private ArrayList<CoinMarketCap> mTargetCoinMarketCaps;


    public CoinVolumePresenter(CoinVolumeContract.View view) {
        mView = view;
        mView.setPresenter(this);

        mCoinMarketCaps = new ArrayList<>();
        mTargetCoinMarketCaps = new ArrayList<>();
    }

    @Override
    public void start() {
        mView.setData(mTargetCoinMarketCaps);
        crawlingCoinList();
    }

    private void crawlingCoinList() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mCoinMarketCaps.clear();
                    mTargetCoinMarketCaps.clear();

                    Document doc = Jsoup.connect(BASE_URL).get();
                    Elements tables = doc.select(".table tbody tr");

                    CoinType[] coinTypes = CoinType.values();


                    for (Element table : tables) {
                        Elements elements = table.getElementsByTag("td");
                        CoinMarketCap coinMarketCap = new CoinMarketCap();
                        coinMarketCap.marketCap = elements.get(2).text();
                        coinMarketCap.price = elements.get(3).text();
                        coinMarketCap.circulatingSupply = elements.get(4).text();
                        coinMarketCap.volume24 = elements.get(5).text();
                        coinMarketCap.changePercent = elements.get(6).text();
                        coinMarketCap.priceGraph7hUrl = elements.last().select(".sparkline").attr("src");
                        coinMarketCap.marketsUrl = elements.get(1).getElementsByTag("a").get(0).attr("href");
                        mCoinMarketCaps.add(coinMarketCap);

                        for (CoinType coinType : coinTypes) {
                            if (coinType.name().toUpperCase().equals(elements.get(1).text().split(" ")[0].toUpperCase())) {
                                coinMarketCap.name = elements.get(1).text().split(" ")[0].toUpperCase();
                                mTargetCoinMarketCaps.add(coinMarketCap);
                            } else if (coinType.name().toUpperCase().equals(elements.get(1).text().split(" ")[1].toUpperCase())) {
                                coinMarketCap.name = elements.get(1).text().split(" ")[1].toUpperCase();
                                mTargetCoinMarketCaps.add(coinMarketCap);
                            }
                        }

                    }

                    mView.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void loadVolume() {
        crawlingCoinList();
    }
}
