package com.blockchain.store.playmarketsdk.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.blockchain.store.playmarketsdk.PlayMarket;
import com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int REQUEST_CODE = 123;

    private BroadcastReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBroadCast();
        findViewById(R.id.button).setOnClickListener(view ->
                new PlayMarket().setAppName("App name")
                        .setDescription("Payment description")
                        .setOjectId("1")
                        .setPriceInUnit("1")
                        .setTransactionType(PlaymarketConstants.TRANSACTION_BUY_OBJECT)
                        .buildWithResult(MainActivity.this, REQUEST_CODE));

        findViewById(R.id.btn_check_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMarket.checkBuy(MainActivity.this);
            }
        });

        findViewById(R.id.btn_check_subscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMarket.getSubscriptionEllapsedTime(MainActivity.this, "1");
            }
        });

    }

    private void initBroadCast() {
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("PlayMarketSDK"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String resultUrl = intent.getStringExtra(PlaymarketConstants.TRANSACTION_RESULT_URL);
            String transactionHash = intent.getStringExtra(PlaymarketConstants.TRANSACTION_RESULT_TXHASH);
        }
    }
}
