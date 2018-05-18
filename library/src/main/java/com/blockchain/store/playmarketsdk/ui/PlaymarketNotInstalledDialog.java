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



public class PlaymarketNotInstalledDialog extends DialogFragment {
    private static final String TAG = "PlaymarketNotInstalledD";
//
//    @BindView(R.id.install_button) Button install_button;
//    @BindView(R.id.cancel_button) Button cancel_button;

    Button install_button;
    Button cancel_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playmarket_not_installed_layout, null);
//        ButterKnife.bind(this, view);
        return view;
    }


    private void openGooglePlay() {
        final String appPackageName = "com.blockchain.store.playmarket";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
        this.dismiss();
    }

//    @OnClick(R.id.install_button)
    void onInstallClicked() {
        openGooglePlay();
    }

//    @OnClick(R.id.cancel_button)
    void onCancelClicked() {
        this.dismiss();
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

}
