package com.melkir.smspush;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignInButton buttonSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        buttonSignIn.setOnClickListener(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "User logged in");
            // user is signed in!
            startActivity(new Intent(this, SMSActivity.class));
            finish();
        } else {
            Log.d(TAG, "User not logged in");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            startActivity(new Intent(this, SMSActivity.class));
            finish();
        } else if (resultCode == RESULT_CANCELED) {
            showSnackbar(getString(R.string.sign_in_cancelled));
        } else if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
            showSnackbar(getString(R.string.no_internet_connection));
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.activity_main), message, Snackbar.LENGTH_SHORT);
    }

    private void signIn() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
                )
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setTheme(R.style.AppTheme)
                .build(), RC_SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }
}
