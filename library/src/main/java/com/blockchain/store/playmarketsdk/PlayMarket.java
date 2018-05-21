package com.blockchain.store.playmarketsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.blockchain.store.playmarketsdk.entities.PaymentObject;
import com.blockchain.store.playmarketsdk.ui.PlaymarketNotInstalledDialog;
import com.blockchain.store.playmarketsdk.ui.PlaymarketPaymentActivity.PlaymarketPaymentActivity;
import com.blockchain.store.playmarketsdk.utilites.Constants;

import static com.blockchain.store.playmarketsdk.helpers.PlayMarketHelper.isPlaymarketInstalled;

public class PlayMarket {
    private static Intent getIntent() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.blockchain.store.playmarket", "com.blockchain.store.playmarket.services.RemoteService"));
        return intent;
    }

    public static void connectToPlayMarket(Context context) {
        Intent intent = getIntent();
        intent.putExtra(Constants.EXTRA_METHOD_NAME, Constants.METHOD_GET_BALANCE);
        context.startService(intent);
    }

    public static void getPlaymarketUser(Context context) {
        Intent intent = getIntent();
        intent.putExtra(Constants.EXTRA_METHOD_NAME, Constants.METHOD_GET_ACCOUNT);
        context.startService(intent);
    }

    public static void createTx(Context context, String transferAmountInWei, String recipientAddress, String userPassword) {
        Intent intent = getIntent();
        intent.putExtra(Constants.EXTRA_METHOD_NAME, Constants.METHOD_TRANSACTION);
        intent.putExtra(Constants.VALUE_TRANSFER_AMOUNT, transferAmountInWei);
        intent.putExtra(Constants.VALUE_RECIPIENT_ADDRESS, recipientAddress);
        intent.putExtra(Constants.VALUE_PASSWORD, userPassword);
        context.startService(intent);

    }

    public static void openPaymentDialogTest(@NonNull String paymentAddress, @NonNull String priceInWei, @NonNull String appName, @NonNull String paymentDescription, @Nullable String paymentId, AppCompatActivity appCompatActivity, int requestCode) {
        PaymentObject paymentObject = new PaymentObject(paymentAddress, priceInWei, appName, paymentDescription, paymentId);
        PlaymarketPaymentActivity.startForResult(appCompatActivity, requestCode, paymentObject);
    }

    public static void openPaymentDialog(@NonNull String paymentAddress, @NonNull String priceInWei, @NonNull String appName, @NonNull String paymentDescription, @Nullable String paymentId, Fragment fragment) {
        openPaymentDialog(paymentAddress, priceInWei, appName, paymentDescription, paymentId, fragment.getActivity().getSupportFragmentManager(), fragment.getActivity().getBaseContext());
    }

    public static void openPaymentDialog(@NonNull String paymentAddress, @NonNull String priceInWei, @NonNull String appName, @NonNull String paymentDescription, @Nullable String paymentId, FragmentManager fragmentManager, Context context) {
        if (isPlaymarketInstalled(context)) {
            PaymentObject paymentObject = new PaymentObject(paymentAddress, priceInWei, appName, paymentDescription, paymentId);
            PlaymarketPaymentActivity.start(context, paymentObject);

        } else {
            new PlaymarketNotInstalledDialog().show(fragmentManager, "install_pm_dialog");
        }
    }

    public static void openPlaymarketNotInstalledDialog(FragmentManager fragmentManager) {
        new PlaymarketNotInstalledDialog().show(fragmentManager, "install_pm_dialog");
    }


}
