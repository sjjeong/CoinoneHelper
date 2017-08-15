package com.googry.coinonehelper.ui.compare_another_exchange;

import com.googry.coinonehelper.data.BithumbTicker;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.data.PoloniexTicker;
import com.googry.coinonehelper.data.TradeSite;
import com.googry.coinonehelper.data.remote.BithumbApiManager;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;
import com.googry.coinonehelper.data.remote.KorbitApiManager;
import com.googry.coinonehelper.data.remote.PoloniexApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public class CompareAnotherExchangePresenter implements CompareAnotherExchangeContract.Presenter {
    private static final int COINONE = 0;
    private static final int BITHUMB = 1;
    private static final int KORBIT = 2;
    private static final int POLONIEX = 3;
    private CompareAnotherExchangeContract.View mView;
    private boolean mAllLoad[] = new boolean[TradeSite.values().length];

    private KorbitTicker mKorbitTicker;


    public CompareAnotherExchangePresenter(CompareAnotherExchangeContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTicker();
    }

    @Override
    public void loadTicker() {
        loadCoinoneTicker();
        loadBithumbTicker();
        loadKorbitTicker();
        loadPoloniexTicker();
    }

    private void loadCoinoneTicker() {
        mAllLoad[COINONE] = false;
        CoinoneApiManager.CoinonePublicApi api = CoinoneApiManager.getApiManager()
                .create(CoinoneApiManager.CoinonePublicApi.class);
        Call<CoinoneTicker> call = api.allTicker();
        call.enqueue(new Callback<CoinoneTicker>() {
            @Override
            public void onResponse(Call<CoinoneTicker> call, Response<CoinoneTicker> response) {
                if (response.body() == null) {
                    loadCoinoneTicker();
                    return;
                }
                mView.showCoinoneTicker(response.body());
                mAllLoad[COINONE] = true;
                isAllLoad();
            }

            @Override
            public void onFailure(Call<CoinoneTicker> call, Throwable t) {
                mView.showToast("코인원 서버가 불안정하여 데이터를 가져오지 못했습니다.");
            }
        });

    }

    private void loadBithumbTicker() {
        mAllLoad[BITHUMB] = false;
        BithumbApiManager.BithumbPublicApi api = BithumbApiManager.getApiManager()
                .create(BithumbApiManager.BithumbPublicApi.class);
        Call<BithumbTicker> call = api.allTicker();
        call.enqueue(new Callback<BithumbTicker>() {
            @Override
            public void onResponse(Call<BithumbTicker> call, Response<BithumbTicker> response) {
                if (response.body() == null) {
                    loadBithumbTicker();
                    return;
                }
                mView.showBithumbTicker(response.body());
                mAllLoad[BITHUMB] = true;
                isAllLoad();
            }

            @Override
            public void onFailure(Call<BithumbTicker> call, Throwable t) {
                mView.showToast("빗썸 서버가 불안정하여 데이터를 가져오지 못했습니다.");
            }
        });
    }

    private void loadKorbitTicker() {
        mKorbitTicker = new KorbitTicker();
        KorbitApiManager.KorbitPublicApi api = KorbitApiManager.getApiManager()
                .create(KorbitApiManager.KorbitPublicApi.class);
        Call<KorbitTicker.Ticker> btcCall = api.btcTicker();
        Call<KorbitTicker.Ticker> ethCall = api.ethTicker();
        Call<KorbitTicker.Ticker> etcCall = api.etcTicker();
        Call<KorbitTicker.Ticker> xrpCall = api.xrpTicker();
        btcCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.btc = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
                mView.showToast("코빗 서버가 불안정하여 데이터를 가져오지 못했습니다.");
            }
        });
        ethCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.eth = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
                mView.showToast("코빗 서버가 불안정하여 데이터를 가져오지 못했습니다.");
            }
        });
        etcCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.etc = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
                mView.showToast("코빗 서버가 불안정하여 데이터를 가져오지 못했습니다.");
            }
        });
        xrpCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.xrp = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
                mView.showToast("코빗 서버가 불안정하여 데이터를 가져오지 못했습니다.");
            }
        });

    }

    private void loadPoloniexTicker() {
        mAllLoad[POLONIEX] = true;

        PoloniexApiManager.PoloniexPublicApi api =
                PoloniexApiManager.getApiManager().create(PoloniexApiManager.PoloniexPublicApi.class);
        Call<PoloniexTicker> call = api.allTicker();
        call.enqueue(new Callback<PoloniexTicker>() {
            @Override
            public void onResponse(Call<PoloniexTicker> call, Response<PoloniexTicker> response) {
                if (response.body() == null) {
                    loadPoloniexTicker();
                    return;
                }
                mView.showPoloniexTicker(response.body());
                mAllLoad[POLONIEX] = true;
                isAllLoad();
            }

            @Override
            public void onFailure(Call<PoloniexTicker> call, Throwable t) {
                t.printStackTrace();

                mView.showToast("폴로닉스 서버가 불안정하여 데이터를 가져오지 못했습니다.");
            }
        });

    }

    private void isReadyShowKorbitTicker() {
        if (mKorbitTicker.btc != null &&
                mKorbitTicker.eth != null &&
                mKorbitTicker.etc != null &&
                mKorbitTicker.xrp != null) {
            mView.showKorbitTicker(mKorbitTicker);
            mAllLoad[KORBIT] = true;
            isAllLoad();
        }
    }

    private void isAllLoad() {
        for (boolean bool : mAllLoad) {
            if (!bool)
                return;
        }
        mView.hideProgress();

    }
}
