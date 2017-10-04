package com.blockchain.store.playmarketsdk.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by samsheff on 28/09/2017.
 */

public class NotPurchasedService extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public NotPurchasedService() {
        super("NotPurchasedService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        System.exit(0);
    }
}
