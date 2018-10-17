package com.blockchain.store.playmarketsdk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants;
import com.blockchain.store.playmarketsdk.ui.PlaymarketPaymentActivity.PlaymarketPaymentActivity;

public class PlayMarket {
    private PaymentObject paymentObject = new PaymentObject();

    public PlayMarket setAppName(String appName) {
        paymentObject.setAppName(appName);
        return this;
    }

    public PlayMarket setPriceInUnit(String priceInUnit) {
        paymentObject.setPriceInUnit(priceInUnit);
        return this;
    }

    public PlayMarket setDescription(String paymentDescription) {
        paymentObject.setPaymentDescription(paymentDescription);
        return this;
    }

    public PlayMarket setOjectId(String objectId) {
        paymentObject.setObjectId(objectId);
        return this;
    }

    public PlayMarket setTransactionType(int transactionType) {
        paymentObject.setTransactionType(transactionType);
        return this;
    }

    public void build(Context context) {
        openPaymentDialog(context);
    }

    public void buildWithResult(Activity activity, int RESULT_CODE) {
        openPaymentDialog(activity, RESULT_CODE);
    }

    public void openPaymentDialog(Context context) {
        PlaymarketPaymentActivity.start(context, this.paymentObject);
    }

    public void openPaymentDialog(Activity activity, int RESULT_CODE) {
        PlaymarketPaymentActivity.startForResult(activity, paymentObject, RESULT_CODE);
    }

    private static Intent getIntent() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.blockchain.store.playmarket.testnet", "com.blockchain.store.playmarket.PurchaseSDK.services.PlayMarketSDK"));
//        intent.setComponent(new ComponentName("com.blockchain.store.playmarket.mainnet", "com.blockchain.store.PurchaseSDK.services.PlayMarketSDK"));
        return intent;
    }

    public static void connectToPlayMarket(Context context) {
        Intent intent = getIntent();
        intent.putExtra(PlaymarketConstants.EXTRA_METHOD_NAME, PlaymarketConstants.METHOD_GET_BALANCE);
        context.startService(intent);
    }


    public static void getPlaymarketUser(Context context) {
        Intent intent = getIntent();
        intent.putExtra(PlaymarketConstants.EXTRA_METHOD_NAME, PlaymarketConstants.METHOD_GET_ACCOUNT);
        context.startService(intent);
    }

    public static void test(Context context) {
        String objectId = "1";
        String priceInDollars = "1";
        String packageName = "com.blockchain.store.playmarket";
        String password = "123123123";

        Intent intent = getIntent();
        intent.putExtra(PlaymarketConstants.EXTRA_METHOD_NAME, PlaymarketConstants.METHOD_TRANSACTION);
        intent.putExtra(PlaymarketConstants.TRANSFER_TRANSACTION_TYPE, PlaymarketConstants.TRANSACTION_BUY_OBJECT);
        intent.putExtra(PlaymarketConstants.TRANSFER_PRICE, priceInDollars);
        intent.putExtra(PlaymarketConstants.TRANSFER_PACKAGE_NAME, packageName);
        intent.putExtra(PlaymarketConstants.TRANSFER_PASSWORD, password);
        intent.putExtra(PlaymarketConstants.TRANSFER_OBJECT_ID, objectId);
        context.startService(intent);

    }

    public static void checkBuy(Context context) {
        String packageName = context.getPackageName();
        packageName = "com.blockchain.store.playmarket";

        Intent intent = getIntent();
        intent.putExtra(PlaymarketConstants.EXTRA_METHOD_NAME, PlaymarketConstants.METHOD_CHECK_BUY)
                .putExtra(PlaymarketConstants.TRANSFER_PACKAGE_NAME, packageName)
                .putExtra(PlaymarketConstants.TRANSFER_OBJECT_ID, "0");
        ContextCompat.startForegroundService(context,intent);
    }

    public static void getSubscriptionEllapsedTime(Context context, String objectId) {
        String packageName = context.getPackageName();
        packageName = "com.blockchain.store.playmarket";

        Intent intent = getIntent();
        intent.putExtra(PlaymarketConstants.EXTRA_METHOD_NAME, PlaymarketConstants.METHOD_CHECK_SUBSCRIPTION)
                .putExtra(PlaymarketConstants.TRANSFER_PACKAGE_NAME, packageName)
                .putExtra(PlaymarketConstants.TRANSFER_OBJECT_ID, objectId);
        ContextCompat.startForegroundService(context,intent);
    }
}
