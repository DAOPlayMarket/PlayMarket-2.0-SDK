package com.blockchain.store.playmarketsdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class PaymentObject implements Parcelable {
    private String paymentAddress;
    private String priceInWei;
    private String appName;
    private String paymentDescription;
    @Nullable
    private String paymentId;

    public PaymentObject(String paymentAddress, String priceInWei, String appName, String paymentDescription, String paymentId) {
        this.paymentAddress = paymentAddress;
        this.priceInWei = priceInWei;
        this.appName = appName;
        this.paymentDescription = paymentDescription;
        this.paymentId = paymentId;
    }

    public String getPaymentAddress() {
        return paymentAddress;
    }

    public String getPriceInWei() {
        return priceInWei;
    }

    public String getAppName() {
        return appName;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public String getPaymentId() {
        return paymentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paymentAddress);
        dest.writeString(this.priceInWei);
        dest.writeString(this.appName);
        dest.writeString(this.paymentDescription);
        dest.writeString(this.paymentId);
    }

    protected PaymentObject(Parcel in) {
        this.paymentAddress = in.readString();
        this.priceInWei = in.readString();
        this.appName = in.readString();
        this.paymentDescription = in.readString();
        this.paymentId = in.readString();
    }

    public static final Creator<PaymentObject> CREATOR = new Creator<PaymentObject>() {
        @Override
        public PaymentObject createFromParcel(Parcel source) {
            return new PaymentObject(source);
        }

        @Override
        public PaymentObject[] newArray(int size) {
            return new PaymentObject[size];
        }
    };
}
