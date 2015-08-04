package com.smartmuki.humans.humansoftheworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends Activity {
    private String FBTAG = "FACEBOOK";
    LoginButton loginButton = null;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null){
            // NOTE: Access token expires in 60 days.
            // TODO: AccessToken.refreshCurrentAccessTokenAsync(); if the token is about to get expired.
            Log.d(FBTAG, "User already logged to facebook successful");

            SharedPreferences prefs = this.getSharedPreferences(SettingsActivity.HelpSettings, Context.MODE_PRIVATE);
            if(prefs.getBoolean(SettingsActivity.HelpSettingsKey, false)) {
                Intent intent = new Intent(getApplicationContext(), FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            loginButton = (LoginButton) findViewById(R.id.login_button);
            //loginButton.setReadPermissions("user_friends");

            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(FBTAG, "Login to facebook successful");
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences(SettingsActivity.HelpSettings, Context.MODE_PRIVATE);
                    if(prefs.getBoolean(SettingsActivity.HelpSettingsKey, false)) {
                        Intent intent = new Intent(getApplicationContext(), FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancel() {
                    Log.d(FBTAG, "Login to facebook has been cancelled by the user.");
                    // Do nothing.
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.e(FBTAG, "Login to facebook FAILED with : " + exception);
                    // TODO: code to retry login
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
