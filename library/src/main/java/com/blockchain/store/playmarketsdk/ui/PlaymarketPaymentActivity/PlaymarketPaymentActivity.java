package com.blockchain.store.playmarketsdk.ui.PlaymarketPaymentActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
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
import com.blockchain.store.playmarketsdk.entities.PaymentObject;
import com.blockchain.store.playmarketsdk.ui.PlaymarketNotInstalledDialog;
import com.blockchain.store.playmarketsdk.utilites.Constants;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.blockchain.store.playmarketsdk.helpers.PlayMarketHelper.isPlaymarketInstalled;
import static com.blockchain.store.playmarketsdk.utilites.Constants.EXTRA_METHOD_ERROR;
import static com.blockchain.store.playmarketsdk.utilites.Constants.EXTRA_METHOD_NAME;
import static com.blockchain.store.playmarketsdk.utilites.Constants.EXTRA_METHOD_RESULT;

public class PlaymarketPaymentActivity extends AppCompatActivity {
    private static final String TAG = "PurchaseInfoFragment";
    private static final String PAYMENT_ARGS = "payment_args";

    TextView priceTextView;
    ImageView applicationIcon;
    TextView purchaseDescriptionTitle;
    TextView purchaseApplicationName;
    ProgressBar accountAddressProgressBar;
    ProgressBar accountBalanceProgressBar;
    LinearLayout buttons_linearLayout;
    TextView accountAddress;
    TextView accountBalance;
    EditText passwordEditText;
    TextView balanceCurrency;
    TextInputLayout password_inputLayout;
    Button cancelTransferButton;
    Button continueTransferButton;
    Button tryAgainTransferButton;
    ProgressBar txProgressBar;
    TextView appDescription;
    TextView hostAddress;
    LinearLayout errorHolder;
    TextView errorText;


    private PaymentObject paymentObject;
    private String userBalance;
    private ERROR_CAUSE errorCause;
    private BroadcastReceiver mMessageReceiver;

    private enum ERROR_CAUSE {
        ACCOUNT, BALANCE, TRANSACTION
    }

    public static void start(Context context, PaymentObject paymentObject) {
        Intent starter = new Intent(context, PlaymarketPaymentActivity.class);
        starter.putExtra(PAYMENT_ARGS, paymentObject);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
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
    }

    private void bindViews() {
        priceTextView = findViewById(R.id.price_textView);
        applicationIcon = findViewById(R.id.app_icon);
        purchaseDescriptionTitle = findViewById(R.id.purchased_content_textVIew);
        purchaseApplicationName = findViewById(R.id.app_name_textView);
        accountAddressProgressBar = findViewById(R.id.account_address_progress_bar);
        accountBalanceProgressBar = findViewById(R.id.account_balance_progress_bar);
        buttons_linearLayout = findViewById(R.id.buttons_linearLayout);
        accountAddress = findViewById(R.id.account_address_field);
        accountBalance = findViewById(R.id.account_balance_field);
        passwordEditText = findViewById(R.id.password_editText);
        balanceCurrency = findViewById(R.id.balance_currency);
        password_inputLayout = findViewById(R.id.password_inputLayout);
        cancelTransferButton = findViewById(R.id.cancel_transfer_button);
        continueTransferButton = findViewById(R.id.continue_transfer_button);
        tryAgainTransferButton = findViewById(R.id.repeat_transfer_button);
        txProgressBar = findViewById(R.id.tx_create_progress_bar);
        appDescription = findViewById(R.id.app_description);
        hostAddress = findViewById(R.id.host_address_field);
        errorHolder = findViewById(R.id.error_holder);
        errorText = findViewById(R.id.error_text);
        cancelTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelButtonClicked();
            }
        });
        continueTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinueTransferClicked();
            }
        });
        tryAgainTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRepeatClicked();
            }
        });
    }

    private void loadFirstData() {
        PlayMarket.connectToPlayMarket(this);
    }

    private void setReceiver() {
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    logAllData(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handleBroadCastData(intent);
            }
        };

    }

    private void setUpFields(PaymentObject paymentObject) {
        purchaseDescriptionTitle.setText(paymentObject.getPaymentDescription());
        purchaseApplicationName.setText(paymentObject.getAppName());
        BigDecimal priceDecimal = new BigDecimal(paymentObject.getPriceInWei());
        priceDecimal = priceDecimal.divide(new BigDecimal(Constants.ETHEREUM_DIVIDER));
        priceTextView.setText(priceDecimal.toEngineeringString());
        hostAddress.setText(paymentObject.getPaymentAddress());
    }

    private void onUserAccountReady(String userAccount) {
        accountAddress.setVisibility(View.VISIBLE);
        accountAddress.setText(userAccount);
        accountAddressProgressBar.setVisibility(View.GONE);
    }

    private void onUserAccountError(String methodResult) {
        if (methodResult.equalsIgnoreCase(Constants.UNKNOWN_HOST_EXCEPTION)) {
            handleUnknownHostException();
        }
        if (methodResult.equalsIgnoreCase(Constants.USER_NOT_PROVIDED_ERROR)) {
            handleUserNotProvided();
        }
        accountAddressProgressBar.setVisibility(View.GONE);
        accountAddress.setText(Constants.DEFAULT_EMPTY_STRING);
        accountBalanceProgressBar.setVisibility(View.GONE);
    }

    private void handleNoUser(String methodResult) {
        onUserAccountError(methodResult);
    }

    private void onUserBalanceReady(String userBalance) {
        BigDecimal priceDecimal = new BigDecimal(userBalance);
        priceDecimal = priceDecimal.divide(new BigDecimal(Constants.ETHEREUM_DIVIDER));
        accountBalance.setText(priceDecimal.toEngineeringString());
        this.userBalance = userBalance;
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
        if (methodResult.equalsIgnoreCase(Constants.UNKNOWN_HOST_EXCEPTION)) {
            handleUnknownHostException();
        } else if (methodResult.equalsIgnoreCase(Constants.USER_NOT_PROVIDED_ERROR)) {
            handleUserNotProvided();
        } else {
            handleDefaultMessage(methodResult);
        }
        accountBalanceProgressBar.setVisibility(View.GONE);
        accountBalance.setText(Constants.DEFAULT_EMPTY_STRING);
    }

    private void onMethodTransactionError(String methodResult) {
        if (methodResult.equalsIgnoreCase(Constants.UNKNOWN_HOST_EXCEPTION)) {
            password_inputLayout.setError(getString(R.string.cant_reach_server));
            password_inputLayout.requestFocus();
        }
        if (methodResult.equalsIgnoreCase(Constants.WRONG_PASSWORD_ERROR)) {
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
            PlayMarket.createTx(this, paymentObject.getPriceInWei(), paymentObject.getPaymentAddress(), passwordEditText.getText().toString());
        }

    }

    private boolean isAllFieldOk() {
        if (accountBalance.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, "Account balance is not loaded", Toast.LENGTH_SHORT).show();

            return false;
        }
        if (accountAddress.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, "Account address is not loaded", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordEditText.setError("This field can not be empty");
            return false;
        }
        if (new BigInteger(userBalance).compareTo(new BigInteger(paymentObject.getPriceInWei())) != 1) {
            Toast.makeText(this, "Not enough balance", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void handleResultMessage(String methodName, String methodResult) {
        errorHolder.setVisibility(View.GONE);
        txProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "handleResultMessage() called with: methodName = [" + methodName + "], methodResult = [" + methodResult + "]");
        switch (methodName) {
            case Constants.METHOD_GET_BALANCE:
                onUserBalanceReady(methodResult);
                break;
            case Constants.METHOD_GET_ACCOUNT:
                onUserAccountReady(methodResult);
                break;
        }
    }

    public void handleErrorMessage(String methodName, String methodResult) {
        txProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "handleErrorMessage() called with: methodName = [" + methodName + "], methodResult = [" + methodResult + "]");
        switch (methodName) {
            case Constants.METHOD_GET_BALANCE:
                errorCause = ERROR_CAUSE.BALANCE;
                onUserBalanceError(methodResult);
                break;
            case Constants.METHOD_GET_ACCOUNT:
                errorCause = ERROR_CAUSE.ACCOUNT;
                onUserAccountError(methodResult);
                break;
            case Constants.METHOD_TRANSACTION:
                errorCause = ERROR_CAUSE.TRANSACTION;
                onMethodTransactionError(methodResult);
                break;
            case Constants.USER_NOT_PROVIDED_ERROR:
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

    private void handleTransactionCreation(String stringExtra) {
        setResult(RESULT_OK);
        finish();
    }

    private void handleBroadCastData(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(EXTRA_METHOD_NAME) && intent.getStringExtra(EXTRA_METHOD_NAME).equalsIgnoreCase(Constants.METHOD_TRANSACTION)) {
                if (!intent.hasExtra(EXTRA_METHOD_ERROR)) {
                    handleTransactionCreation(intent.getStringExtra(EXTRA_METHOD_RESULT));
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

    private void logAllData(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object keyValue = extras.get(key);
                Log.d(TAG, "logAllData: key = " + key + ", value = " + keyValue);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("RemoteService"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}
