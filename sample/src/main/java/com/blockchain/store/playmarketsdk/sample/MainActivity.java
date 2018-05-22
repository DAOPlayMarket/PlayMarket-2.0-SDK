package com.blockchain.store.playmarketsdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blockchain.store.playmarketsdk.PlayMarket;
import com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlayMarket.openPaymentScreen("0xb1215133D580Ac70811323aBbc3D87e2F6B39BD1", "1000000000000000000",
                "My App Name", "My PAyment description. Please buy it!", null, this, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        String result = "";
        if (intent != null && intent.hasExtra(PlaymarketConstants.PM_TX_RESULT)) {
            result = intent.getStringExtra(PlaymarketConstants.PM_TX_RESULT);
        }
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + result + "]");
    }
}
