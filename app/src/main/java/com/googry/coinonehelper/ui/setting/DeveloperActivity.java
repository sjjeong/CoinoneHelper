package com.googry.coinonehelper.ui.setting;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.databinding.DeveloperActivityBinding;

/**
 * Created by seokjunjeong on 2017. 8. 23..
 */

public class DeveloperActivity extends AppCompatActivity {
    private DeveloperActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.developer_activity);
        mBinding.setActivity(this);
    }

    public void onSendEmailClick(View v){
        Uri uri = Uri.parse("mailto:" + getString(R.string.developer_email_address));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "To Developer of CoinoneHelper");
        startActivity(intent);
    }

    public void onDonateClick(View v){
        String link = "";
        switch (v.getId()) {
            case R.id.btn_btc_copy:{
                link = getString(R.string.donate_btc);
            }
            break;
            case R.id.btn_bch_copy:{
                link = getString(R.string.donate_bch);
            }
            break;
            case R.id.btn_eth_copy:{
                link = getString(R.string.donate_eth);
            }
            break;
            case R.id.btn_etc_copy:{
                link = getString(R.string.donate_etc);
            }
            break;
            case R.id.btn_xrp_copy:{
                link = getString(R.string.donate_xrp);
            }
            break;
            case R.id.btn_xrp_destination_copy:{
                link = getString(R.string.donate_xrp_destination);
            }
            break;
        }

        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("donate_address", link);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(), "기부 주소가 복사 됐습니다.", Toast.LENGTH_SHORT).show();
    }
}
