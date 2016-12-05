package com.melkir.smspush;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;

public class SMSActivity extends AppCompatActivity {

    private static final String TAG = SMSActivity.class.getSimpleName();
    private static final int RC_HANDLE_SMS_PERM = 2;
    private SmsManager smsManager = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        checkSMSPermission();
        handleOnResumeNotification();
    }

    /**
     * Check for the sms permission before accessing sending sms. If the
     * permission is not granted yet, request permission.
     */
    private void checkSMSPermission() {
        int rc = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS);
        if (rc != PackageManager.PERMISSION_GRANTED) {
            requestSendSMSPermission();
        }
    }

    /**
     * If a notification message is tapped, any data accompanying the notification
     * message is available in the intent extras. In this sample the launcher
     * intent is fired when the notification is tapped, so any accompanying data would
     * be handled here. If you want a different intent fired, set the click_action
     * field of the notification message to the desired intent. The launcher intent
     * is used when no click_action is specified.
     * Handle possible data accompanying notification message.
     */
    private void handleOnResumeNotification() {
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, key + ": " + value);
            }
            String phoneNumber, smsBody;
            phoneNumber = (String) getIntent().getExtras().get("number");
            smsBody = (String) getIntent().getExtras().get("message");
            if (phoneNumber != null && smsBody != null) {
                sendSMS(phoneNumber, smsBody);
            }
        }
    }

    /**
     * Handles the requesting of the send sms permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestSendSMSPermission() {
        Log.w(TAG, "SMS_SEND permission is not granted. Requesting permission");

        final String[] permissions = new String[]{android.Manifest.permission.SEND_SMS};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_SMS_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions, RC_HANDLE_SMS_PERM);
            }
        };

        View v = getWindow().getDecorView().getRootView();
        Snackbar.make(v, R.string.permission_sms_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    private void sendSMS(String phoneNumber, String smsBody) {
        smsManager.sendTextMessage(phoneNumber, "FCM", smsBody, null, null);
    }

    public void logToken(View view) {
        String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg);
        Toast.makeText(SMSActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void signOut(View view) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                // user is now signed out
                startActivity(new Intent(SMSActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}
