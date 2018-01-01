package com.googry.coinonehelper.ui.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.Injection;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.MarketAccount;
import com.googry.coinonehelper.databinding.SettingActivityBinding;
import com.googry.coinonehelper.ui.setting.adapter.CoinUnitAlarmAdapter;
import com.googry.coinonehelper.ui.widget.MarketAccountRegisterDialog;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 8. 19..
 */

public class SettingActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    public ObservableField<String> marketAccount = new ObservableField<>();
    private SettingActivityBinding mBinding;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;
    private Realm mRealm;
    private MarketAccount mAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_activity);
        mBinding.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mBinding.setActivity(this);
        mRealm = Injection.getSecureRealm();
        mAccount = mRealm.where(MarketAccount.class).findFirst();


        createGoogleApiClient();
        initFirebaseUser();
        initCoinUnitAlarm();
        initMarketAccount();
    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    private void initMarketAccount() {
        if (BuildConfig.FLAVOR.equals("coinone")) {
            if (mAccount != null) {
                marketAccount.set(getString(R.string.unregister_account));
            } else {
                marketAccount.set(getString(R.string.register_account));
            }
        } else {
            findViewById(R.id.ll_market_account).setVisibility(View.GONE);
        }
    }

    private void initCoinUnitAlarm() {
        CoinUnitAlarmAdapter adapter = new CoinUnitAlarmAdapter(mRealm);
        mBinding.rvCoinPriceUnitAlarm.setAdapter(adapter);
    }

    private void initFirebaseUser() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        setFirebaseUser(mFirebaseUser);
    }

    private void setFirebaseUser(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            noUser();
        } else {
            hasUser(firebaseUser);
        }
    }

    private void createGoogleApiClient() {
        if (mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.auth_google_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    private void signInForGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOutForGoogle() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi
                .signOut(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
        noUser();
    }


    private void noUser() {
        mBinding.llAccount.setVisibility(View.GONE);
        mBinding.btnGoogle.setText(R.string.sign_in_for_google);
        mBinding.llModifyNickname.setVisibility(View.GONE);
        mBinding.etNickname.setText("");
    }

    private void hasUser(FirebaseUser firebaseUser) {
        mBinding.llAccount.setVisibility(View.VISIBLE);
        mBinding.btnGoogle.setText(R.string.sign_out);
        mBinding.tvEmail.setText(mFirebaseUser.getEmail());
        mBinding.tvNickname.setText(firebaseUser.getDisplayName());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mFirebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    failedSignInForGoogle();
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.google_sign_in_success, Toast.LENGTH_LONG).show();
                                    initFirebaseUser();
                                }
                            }
                        });
            } else {
                // Google Sign-In failed
                failedSignInForGoogle();
            }
        }
    }

    private void failedSignInForGoogle() {
        Toast.makeText(getApplicationContext(), R.string.google_sign_in_failed, Toast.LENGTH_LONG).show();
    }

    // databinding
    public void onBackClick(View v) {
        finish();
    }

    // databinding
    public void onGoogleBtnClick(View v) {
        Button button = (Button) v;
        if (button.getText().toString().equals(getString(R.string.sign_in_for_google))) {
            // 구글 로그인
            signInForGoogle();
        } else {
            // 구글 로그아웃
            signOutForGoogle();
        }
    }

    // databinding
    public void onChangeNicknameClick(View v) {
        if (mBinding.llModifyNickname.getVisibility() == View.GONE) {
            mBinding.llModifyNickname.setVisibility(View.VISIBLE);
            mBinding.etNickname.requestFocus();
        } else {
            mBinding.llModifyNickname.setVisibility(View.GONE);
        }
    }

    // databinding
    public void onConfirmNicknameChangeClick(View v) {
        final String nickname = mBinding.etNickname.getText().toString();

        if (TextUtils.isEmpty(nickname)) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setMessage(getString(R.string.nickname_changing));
        progressDialog.show();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nickname)
                .build();

        mFirebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.nickname_change_success, Toast.LENGTH_LONG).show();
                            mBinding.tvNickname.setText(nickname);
                            mBinding.llModifyNickname.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    R.string.nickname_change_failed, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    // databinding
    public void onMarketAccountRegisterClick(View v) {
        if (marketAccount.get().equals(getString(R.string.register_account))) {
            MarketAccountRegisterDialog dialog = MarketAccountRegisterDialog.newInstance();
            dialog.setOnRequestResultListener(new MarketAccountRegisterDialog.OnRequestResultListener() {
                @Override
                public void onRequestResultListener(final MarketAccount account) {
                    marketAccount.set(getString(R.string.unregister_account));
                    mAccount = account;
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealm(account);
                        }
                    });
                }
            });
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        } else {
            new AlertDialog.Builder(SettingActivity.this)
                    .setMessage(R.string.do_you_want_to_unregister)
                    .setPositiveButton(R.string.unregister, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    if (mAccount != null) {
                                        marketAccount.set(getString(R.string.register_account));
                                        mAccount.deleteFromRealm();
                                        mAccount = null;
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

    }

    public void onDeveloperPageClick(View v) {
        startActivity(new Intent(getApplicationContext(), DeveloperActivity.class));
    }

}
