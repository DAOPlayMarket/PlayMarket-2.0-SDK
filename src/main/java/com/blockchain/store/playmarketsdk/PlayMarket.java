package com.blockchain.store.playmarketsdk;

import android.app.Activity;
import android.content.Intent;

import com.blockchain.store.playmarketsdk.services.PurchaseVerifierService;

/**
 * Created by samsheff on 28/09/2017.
 */

public class PlayMarket {

    public static void startPurchaseVerifierService(Activity activity) {
        Intent i = new Intent(activity, PurchaseVerifierService.class);
        activity.startService(i);
    }
}
