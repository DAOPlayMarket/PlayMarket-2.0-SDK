package com.blockchain.store.playmarketsdk.ui;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blockchain.store.playmarketsdk.R;
import com.blockchain.store.playmarketsdk.ui.PlaymarketPaymentActivity.PlaymarketPaymentActivity;


public class PlaymarketNotInstalledDialog extends DialogFragment {
    private static final String TAG = "PlaymarketNotInstalledD";

    private Button installBtn;
    private Button cancelBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playmarket_not_installed_layout, null);
        bindViews(view);
        setListeners();
        return view;
    }

    private void bindViews(View view) {
        installBtn = view.findViewById(R.id.install_button);
        cancelBtn = view.findViewById(R.id.cancel_button);
    }

    private void setListeners() {
        installBtn.setOnClickListener(v -> openGooglePlay());
        cancelBtn.setOnClickListener(v -> exit());

    }

    private void openGooglePlay() {
        final String appPackageName = "com.blockchain.store.playmarket.mainnet";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
        exit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void exit() {
        dismiss();
        if (getActivity() instanceof PlaymarketPaymentActivity) {
            ((PlaymarketPaymentActivity) getActivity()).onDestroy();
        }
    }

}
