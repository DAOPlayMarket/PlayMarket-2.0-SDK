package com.blockchain.store.playmarketsdk.ui.PlaymarketPaymentActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarketsdk.PlayMarket;
import com.blockchain.store.playmarketsdk.R;
import com.blockchain.store.playmarketsdk.PaymentObject;
import com.blockchain.store.playmarketsdk.receiver.PlaymarketResultReceiver;
import com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants;
import com.blockchain.store.playmarketsdk.ui.PlaymarketNotInstalledDialog;

import java.math.BigDecimal;

import static com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants.EXTRA_METHOD_ERROR;
import static com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants.EXTRA_METHOD_NAME;
import static com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants.EXTRA_METHOD_RESULT;
import static com.blockchain.store.playmarketsdk.helpers.PlayMarketHelper.isPlaymarketInstalled;
import static com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants.TRANSACTION_RESULT_TXHASH;
import static com.blockchain.store.playmarketsdk.utilites.PlaymarketConstants.TRANSACTION_RESULT_URL;


public class PlaymarketPaymentActivity extends AppCompatActivity {
    private static final String TAG = "PurchaseInfoFragment";
    private static final String PAYMENT_ARGS = "payment_args";

    private TextView priceTextView;
    private ImageView applicationIcon;
    private TextView purchaseDescriptionTitle;
    private TextView purchaseApplicationName;
    private ProgressBar accountAddressProgressBar;
    private ProgressBar accountBalanceProgressBar;
    private TextView accountAddress;
    private TextView accountBalance;
    private EditText passwordEditText;
    private TextView balanceCurrency;
    private TextInputLayout password_inputLayout;
    private Button cancelTransferButton;
    private Button continueTransferButton;
    private Button tryAgainTransferButton;
    private ProgressBar txProgressBar;
    private LinearLayout errorHolder;
    private TextView errorText;

    private PaymentObject paymentObject;
    private String userBalance;
    private ERROR_CAUSE errorCause;
    private BroadcastReceiver mMessageReceiver;
    private BroadcastReceiver globalBroadcastReceiver;

    private enum ERROR_CAUSE {
        ACCOUNT, BALANCE, TRANSACTION
    }

    public static void start(Context context, PaymentObject paymentObject) {
        Intent starter = new Intent(context, PlaymarketPaymentActivity.class);
        starter.putExtra(PAYMENT_ARGS, paymentObject);
        context.startActivity(starter);
    }

    public static void startForResult(Activity activity, PaymentObject paymentObject, int RESULT_CODE) {
        Intent starter = new Intent(activity.getBaseContext(), PlaymarketPaymentActivity.class);
        starter.putExtra(PAYMENT_ARGS, paymentObject);
        activity.startActivityForResult(starter, RESULT_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_purchase_info);
        if (!isPlaymarketInstalled(this)) {
            new PlaymarketNotInstalledDialog().show(getSupportFragmentManager(), "install_pm_dialog");
        }
        bindViews();
        paymentObject = getIntent().getParcelableExtra(PAYMENT_ARGS);
        setReceiver();
        setUpFields(paymentObject);
        loadFirstData();
        createTestReceiver();
    }

    private void createTestReceiver() {
        globalBroadcastReceiver = new PlaymarketResultReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("PlayMarketSDK");
        this.registerReceiver(globalBroadcastReceiver, filter);
    }

    private void bindViews() {
        priceTextView = findViewById(R.id.price_textView);
        applicationIcon = findViewById(R.id.app_icon);
        purchaseDescriptionTitle = findViewById(R.id.purchased_content_textVIew);
        purchaseApplicationName = findViewById(R.id.app_name_textView);
        accountAddressProgressBar = findViewById(R.id.account_address_progress_bar);
        accountBalanceProgressBar = findViewById(R.id.account_balance_progress_bar);
        accountAddress = findViewById(R.id.account_address_field);
        accountBalance = findViewById(R.id.account_balance_field);
        passwordEditText = findViewById(R.id.password_editText);
        balanceCurrency = findViewById(R.id.balance_currency);
        password_inputLayout = findViewById(R.id.password_inputLayout);
        cancelTransferButton = findViewById(R.id.cancel_transfer_button);
        continueTransferButton = findViewById(R.id.continue_transfer_button);
        tryAgainTransferButton = findViewById(R.id.repeat_transfer_button);
        txProgressBar = findViewById(R.id.tx_create_progress_bar);
        errorHolder = findViewById(R.id.error_holder);
        errorText = findViewById(R.id.error_text);

        cancelTransferButton.setOnClickListener(v -> onCancelButtonClicked());
        continueTransferButton.setOnClickListener(v -> onContinueTransferClicked());
        tryAgainTransferButton.setOnClickListener(v -> onRepeatClicked());
    }

    private void loadFirstData() {
        PlayMarket.connectToPlayMarket(this);
    }

    private void setReceiver() {
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
                handleBroadCastData(intent);
            }
        };

    }

    private void setUpFields(PaymentObject paymentObject) {
        purchaseDescriptionTitle.setText(paymentObject.getPaymentDescription());
        purchaseApplicationName.setText(paymentObject.getAppName());
        BigDecimal priceDecimal = new BigDecimal(paymentObject.getPriceInUnit());
        priceTextView.setText(priceDecimal.toEngineeringString());
    }

    private void onUserAccountReady(String userAccount) {
        accountAddress.setVisibility(View.VISIBLE);
        accountAddress.setText(userAccount);
        accountAddressProgressBar.setVisibility(View.GONE);
    }

    private void onUserAccountError(String methodResult) {
        if (methodResult.equalsIgnoreCase(PlaymarketConstants.UNKNOWN_HOST_EXCEPTION)) {
            handleUnknownHostException();
        }
        if (methodResult.equalsIgnoreCase(PlaymarketConstants.USER_NOT_PROVIDED_ERROR)) {
            handleUserNotProvided();
        }
        accountAddressProgressBar.setVisibility(View.GONE);
        accountAddress.setText(PlaymarketConstants.DEFAULT_EMPTY_STRING);
        accountBalanceProgressBar.setVisibility(View.GONE);
    }

    private void handleNoUser(String methodResult) {
        onUserAccountError(methodResult);
    }

    private void onUserBalanceReady(String userBalanceInWei) {
        BigDecimal priceDecimal = new BigDecimal(userBalanceInWei);
        accountBalance.setText(priceDecimal.toEngineeringString());
        this.userBalance = userBalanceInWei;
        balanceCurrency.setVisibility(View.VISIBLE);
        accountBalanceProgressBar.setVisibility(View.GONE);
        accountBalance.setVisibility(View.VISIBLE);
        showPasswordField();
    }

    private void showPasswordField() {
        password_inputLayout.setVisibility(View.VISIBLE);
        continueTransferButton.setVisibility(View.VISIBLE);
    }

    private void onUserBalanceError(String methodResult) {
        if (methodResult.equalsIgnoreCase(PlaymarketConstants.UNKNOWN_HOST_EXCEPTION)) {
            handleUnknownHostException();
        } else if (methodResult.equalsIgnoreCase(PlaymarketConstants.USER_NOT_PROVIDED_ERROR)) {
            handleUserNotProvided();
        } else {
            handleDefaultMessage(methodResult);
        }
        accountBalanceProgressBar.setVisibility(View.GONE);
        accountBalance.setText(PlaymarketConstants.DEFAULT_EMPTY_STRING);
    }

    private void onMethodTransactionError(String methodResult) {
        if (methodResult.equalsIgnoreCase(PlaymarketConstants.UNKNOWN_HOST_EXCEPTION)) {
            password_inputLayout.setError(getString(R.string.cant_reach_server));
            password_inputLayout.requestFocus();
        }
        if (methodResult.equalsIgnoreCase(PlaymarketConstants.WRONG_PASSWORD_ERROR)) {
            password_inputLayout.setError(getString(R.string.wrong_password));
            password_inputLayout.requestFocus();
            passwordEditText.setText("");
        }
        continueTransferButton.setVisibility(View.VISIBLE);
        txProgressBar.setVisibility(View.GONE);
    }

    void onCancelButtonClicked() {
        finish();
    }

    void onContinueTransferClicked() {
        if (isAllFieldOk()) {
            continueTransferButton.setVisibility(View.GONE);
            txProgressBar.setVisibility(View.VISIBLE);
            PlayMarket.test(this);
        }

    }

    private boolean isAllFieldOk() {
        if (accountBalance.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, R.string.account_balance_not_loaded, Toast.LENGTH_SHORT).show();

            return false;
        }
        if (accountAddress.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, R.string.account_address_not_loaded, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordEditText.setError(getString(R.string.empty_field));
            return false;
        }
        if (Double.parseDouble(userBalance) > Double.parseDouble(paymentObject.getPriceInUnit())) {
            Toast.makeText(this, R.string.not_enough_balance, Toast.LENGTH_SHORT).show();
            return false;
        }
//       if (new BigInteger(userBalance).compareTo(new BigInteger(paymentObject.getPriceInUnit())) != 1) {
//            Toast.makeText(this, R.string.not_enough_balance, Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;
    }

    public void handleResultMessage(String methodName, String methodResult) {
        errorHolder.setVisibility(View.GONE);
        txProgressBar.setVisibility(View.GONE);
        switch (methodName) {
            case PlaymarketConstants.METHOD_GET_BALANCE:
                onUserBalanceReady(methodResult);
                break;
            case PlaymarketConstants.METHOD_GET_ACCOUNT:
                onUserAccountReady(methodResult);
                break;
        }
    }

    public void handleErrorMessage(String methodName, String methodResult) {
        txProgressBar.setVisibility(View.GONE);
        switch (methodName) {
            case PlaymarketConstants.METHOD_GET_BALANCE:
                errorCause = ERROR_CAUSE.BALANCE;
                onUserBalanceError(methodResult);
                break;
            case PlaymarketConstants.METHOD_GET_ACCOUNT:
                errorCause = ERROR_CAUSE.ACCOUNT;
                onUserAccountError(methodResult);
                break;
            case PlaymarketConstants.METHOD_TRANSACTION:
                errorCause = ERROR_CAUSE.TRANSACTION;
                onMethodTransactionError(methodResult);
                break;
            case PlaymarketConstants.USER_NOT_PROVIDED_ERROR:
                errorCause = ERROR_CAUSE.ACCOUNT;
                handleNoUser(methodResult);
                break;
        }
        showRepeatButton();
    }

    private void showRepeatButton() {
        continueTransferButton.setVisibility(View.GONE);
        tryAgainTransferButton.setVisibility(View.VISIBLE);
    }

    void onRepeatClicked() {
        tryAgainTransferButton.setVisibility(View.GONE);
        txProgressBar.setVisibility(View.VISIBLE);
        switch (errorCause) {
            case BALANCE:
                PlayMarket.connectToPlayMarket(this);
                break;
            case ACCOUNT:
                PlayMarket.getPlaymarketUser(this);
                break;
            case TRANSACTION:
                onContinueTransferClicked();
                break;
        }
    }

    private void handleUnknownHostException() {
        errorHolder.setVisibility(View.VISIBLE);
        errorText.setText(R.string.cant_reach_server);
    }


    private void handleDefaultMessage(String methodResult) {
        errorHolder.setVisibility(View.VISIBLE);
        errorText.setText(methodResult);
    }

    private void handleUserNotProvided() {
        errorHolder.setVisibility(View.VISIBLE);
        errorText.setText(R.string.login_not_provided_error);
    }

    private void handleTransactionCreation(Intent intent) {
        Intent resultIntent = new Intent();

        String rinkebyUrl = intent.getStringExtra(TRANSACTION_RESULT_URL);
        String txHash = intent.getStringExtra(TRANSACTION_RESULT_TXHASH);

        resultIntent.putExtra(TRANSACTION_RESULT_URL, rinkebyUrl);
        resultIntent.putExtra(TRANSACTION_RESULT_TXHASH, txHash);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void handleBroadCastData(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(EXTRA_METHOD_NAME) && intent.getStringExtra(EXTRA_METHOD_NAME).equalsIgnoreCase(PlaymarketConstants.METHOD_TRANSACTION)) {
                if (!intent.hasExtra(EXTRA_METHOD_ERROR)) {
                    handleTransactionCreation(intent);
                } else {
                    handleErrorMessage(intent.getStringExtra(EXTRA_METHOD_NAME), intent.getStringExtra(EXTRA_METHOD_ERROR));
                }
                return;
            }
            if (intent.hasExtra(EXTRA_METHOD_ERROR)) {
                handleErrorMessage(intent.getStringExtra(EXTRA_METHOD_NAME), intent.getStringExtra(EXTRA_METHOD_ERROR));
            }
            if (intent.hasExtra(EXTRA_METHOD_RESULT)) {
                handleResultMessage(intent.getStringExtra(EXTRA_METHOD_NAME), intent.getStringExtra(EXTRA_METHOD_RESULT));
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("PlayMarketSDK"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}
