package com.blockchain.store.playmarketsdk.callback;

import android.support.annotation.Nullable;

public interface PaymentDialogCallback {
    public void onPurchaseSuccessfully(@Nullable String paymentId);
    public void onPurchaseCanceled();
}
