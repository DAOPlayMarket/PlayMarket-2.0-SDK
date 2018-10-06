package com.blockchain.store.playmarketsdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class PaymentObject implements Parcelable {
    private String paymentAddress;
    private String priceInUnit;
    private String appName;
    private String paymentDescription;
    private int transactionType;
    private String objectId;
    private String packageName;

    public String getPaymentAddress() {
        return paymentAddress;
    }

    public void setPaymentAddress(String paymentAddress) {
        this.paymentAddress = paymentAddress;
    }

    public String getPriceInUnit() {
        return priceInUnit;
    }

    public void setPriceInUnit(String priceInUnit) {
        this.priceInUnit = priceInUnit;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paymentAddress);
        dest.writeString(this.priceInUnit);
        dest.writeString(this.appName);
        dest.writeString(this.paymentDescription);
        dest.writeInt(this.transactionType);
        dest.writeString(this.objectId);
        dest.writeString(this.packageName);
    }

    public PaymentObject() {
    }

    protected PaymentObject(Parcel in) {
        this.paymentAddress = in.readString();
        this.priceInUnit = in.readString();
        this.appName = in.readString();
        this.paymentDescription = in.readString();
        this.transactionType = in.readInt();
        this.objectId = in.readString();
        this.packageName = in.readString();
    }

    public static final Parcelable.Creator<PaymentObject> CREATOR = new Parcelable.Creator<PaymentObject>() {
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
