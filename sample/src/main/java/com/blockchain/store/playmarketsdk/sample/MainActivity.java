package com.blockchain.store.playmarketsdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blockchain.store.playmarketsdk.PlayMarket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(v -> PlayMarket.test(MainActivity.this));
    }
}
