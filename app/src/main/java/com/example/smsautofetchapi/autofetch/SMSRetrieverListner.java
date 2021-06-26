package com.example.smsautofetchapi.autofetch;

public interface SMSRetrieverListner {

    void onSMSReceived(String otpValue);

    void onSMSRetriverTimeOut();
}
