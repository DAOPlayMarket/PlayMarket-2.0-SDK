package com.blockchain.store.playmarketsdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by samsheff on 28/09/2017.
 */

public class PlayMarket {

    public static void startPurchaseVerifierService(Activity activity, String appId, String idCtg, String hashIpfs) {
        Intent i = new Intent();
        i.setClassName("com.blockchain.store.playstore", "com.blockchain.store.playstore.services.PurchaseVerifierService");
        i.setData(Uri.parse(appId + ":" + idCtg + ":" + hashIpfs + ":" + activity.getPackageName()));
        activity.startService(i);
    }
}
