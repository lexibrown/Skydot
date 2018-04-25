package com.comp.lexi.skydot.utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.comp.lexi.skydot.data.Transaction;

import java.util.Arrays;
import java.util.List;

public class Variables {

    public static final String AUTH_URL = "http://auth-skydot.eastus.cloudapp.azure.com";
    public static final String BASE_URL = "http://mobile-skydot.eastus.cloudapp.azure.com";

    public static final String LOGIN = "/auth/login";
    public static final String LOGOUT = "/auth/logout";

    public static final String ACCOUNT = "/account";
    public static final String ACCOUNT_DETAILS = "/account/details";

    public static final String TRANSFER = "/transfer";
    public static final String BILL_PAYMENT = "/bill";

    public static final String BILL_PAYEE = "/bill/payee";
    public static final String BILL_PAYEE_SEARCH = "/bill/payee/search";

    public static final String CREATE = "/user/create";
    public static final String DELETE = "/user/delete";

    public static final String USERID = "USER_ID";
    public static final String ACCOUNTID = "ACCOUNT_ID";

    public enum AccountType {
        BANKING("Banking"), BORROWING("Borrowing"), INVESTING("Investing");

        private String value;

        private AccountType(String val) {
            this.value = val;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static final String[] accountTypes = { AccountType.BANKING.getValue(), AccountType.BORROWING.getValue(),
            AccountType.INVESTING.getValue() };

    public class TransactionParams {
        public static final String FROM = "from_account";
        public static final String TO = "to_account";
        public static final String PAYEE = "payee";
        public static final String AMOUNT = "amount";
        public static final String CURRENCY = "currency";
    }

    public static final List<String> profileParams = Arrays.asList(TransactionParams.FROM, TransactionParams.TO,
            TransactionParams.PAYEE, TransactionParams.AMOUNT, TransactionParams.CURRENCY);

    public static final int TIMEOUT_TIME = 5 * 60; // 5 minutes
    public static int REQUEST_TIMEOUT = 20 * 1000; // 20 seconds

    public static final int USERNAME_LENGTH = 16;

    public static final int MAX_PASSWORD_LENGTH = 16;
    public static final int MIN_PASSWORD_LENGTH = 5;

    public static final String defaultName = "default";
    public static final String defaultPassword = "password";

    public static final RetryPolicy retryPolicy = new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    );

}
