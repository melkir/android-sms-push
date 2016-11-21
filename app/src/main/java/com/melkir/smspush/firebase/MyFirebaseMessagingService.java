package com.melkir.smspush.firebase;

/**
 * Created by melkir on 21/11/16.
 */

import android.telephony.SmsManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private SmsManager smsManager = SmsManager.getDefault();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*
         There are two types of messages data messages and notification messages.
         Data messages are handled here in onMessageReceived whether the app is in the foreground or background.
         Notification messages are only received here in onMessageReceived when the app is in the foreground.
         When the app is in the background an automatically generated notification is displayed.
         When the user taps on the notification they are returned to the app.
         Messages containing both notification and data payloads are treated as notification messages.
         The Firebase console always sends notification messages.
         For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        */

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> smsData = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + smsData);
            sendSMS(smsData.get("number"), smsData.get("message"));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendSMS(String phoneNumber, String smsBody) {
        smsManager.sendTextMessage(phoneNumber, "FCM", smsBody, null, null);
    }

}