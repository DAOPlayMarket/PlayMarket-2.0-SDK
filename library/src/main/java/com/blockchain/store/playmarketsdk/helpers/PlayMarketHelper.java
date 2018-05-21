package com.blockchain.store.playmarketsdk.helpers;

import android.content.Context;
import android.content.pm.PackageManager;

public class PlayMarketHelper {
    public static boolean isPlaymarketInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.blockchain.store.playmarket", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
