package com.example.androidsmsverificationapi.autofill;

public interface SMSRetrieverListner {

    void onSMSReceived(String otpValue);

    void onSMSRetriverTimeOut();
}
