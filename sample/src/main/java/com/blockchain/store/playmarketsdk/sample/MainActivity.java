package com.blockchain.store.playmarketsdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blockchain.store.playmarketsdk.PlayMarket;
import com.blockchain.store.playmarketsdk.entities.PlaymarketConstants;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlayMarket().setAppName("App name")
                        .setDescription("Payment description")
                        .setOjectId("10")
                        .setPriceInUnit("1")
                        .setTransactionType(PlaymarketConstants.TRANSACTION_BUY_OBJECT)
                        .buildWithResult(MainActivity.this,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        String result = "";
        String paymentId = "";

    }
}
