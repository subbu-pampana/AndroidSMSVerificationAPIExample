package com.example.smsautofetchapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.smsautofetchapi.autofetch.HashCodeHelper;
import com.example.smsautofetchapi.autofetch.MySMSBroadcastReceiver;
import com.example.smsautofetchapi.autofetch.SMSRetrieverListner;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private String getHashKey(Activity activity) {
        HashCodeHelper appSignatureHelper = new HashCodeHelper(activity);
        String hashCode = appSignatureHelper.getAppSignaturesList().get(0);
        return hashCode;
    }

    private void startSMSRetriverListner(Activity activity) {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(activity);
        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();
        // Listen for success/failure of the start Task. If in a background thread, this
         // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });

        MySMSBroadcastReceiver myBroadcastReceiver = new MySMSBroadcastReceiver();
        activity.registerReceiver(myBroadcastReceiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
        myBroadcastReceiver.startListerning(new SMSRetrieverListner() {
            @Override
            public void onSMSReceived(String message) {

            }

            @Override
            public void onSMSRetriverTimeOut() {

            }
        });
    }

}