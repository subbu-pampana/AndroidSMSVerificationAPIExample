package com.example.smsautofetchapi.autofetch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class MySMSBroadcastReceiver extends BroadcastReceiver  {
    public SMSRetrieverListner smsRetrieverListner;

    public void  startListerning(SMSRetrieverListner otpReceiveListener) {
        this.smsRetrieverListner = otpReceiveListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                if (status != null)
                    switch (status.getStatusCode()) {
                        case CommonStatusCodes.SUCCESS:
                            // Get SMS message contents
                            String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                            // Extract one-time code from the message and complete verification
                            // by sending the code back to your server.
                            if (message != null) {
                                    if (this.smsRetrieverListner != null){
                                        this.smsRetrieverListner.onSMSReceived(message);
                                    }
                            }else{
                                if (this.smsRetrieverListner != null){
                                    this.smsRetrieverListner.onSMSReceived(null);
                                }
                            }
                            break;
                        case CommonStatusCodes.TIMEOUT:
                            // Waiting for SMS timed out (5 minutes)
                            // Handle the error ...
                            if (this.smsRetrieverListner != null){
                                this.smsRetrieverListner.onSMSRetriverTimeOut();
                            }
                            break;
                    }
            }
        }
    }
}
