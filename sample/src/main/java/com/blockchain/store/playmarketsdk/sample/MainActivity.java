package com.blockchain.store.playmarketsdk.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blockchain.store.playmarketsdk.PlayMarket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlayMarket.openPaymentDialog("0xb1215133D580Ac70811323aBbc3D87e2F6B39BD1", "1000000000000000000",
                "My App Name", "My PAyment description. Please buy it!", null, this);
    }

}
