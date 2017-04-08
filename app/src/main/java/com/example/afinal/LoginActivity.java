package com.example.afinal;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


public class LoginActivity extends Activity
{

    boolean loginsuccess=false;
    private CallbackManager mCallbackManager;
    private AccessToken mToken = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);
        mCallbackManager = CallbackManager.Factory.create();
        mToken = AccessToken.getCurrentAccessToken();

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        if (loginButton != null) {
            loginButton.setReadPermissions("public_profile", "user_friends", "email");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    GraphRequest request;
                    mToken = loginResult.getAccessToken();
                    request = GraphRequest.newMeRequest(mToken, jsonObjectCallback);
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday,cover");
                    request.setParameters(parameters);
                    request.executeAsync();
                    finish();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                }
            });
        } else {
            GraphRequest request;
            request = GraphRequest.newMeRequest(mToken, jsonObjectCallback);
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
            finish();


        }
        ;





    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    GraphRequest.GraphJSONObjectCallback jsonObjectCallback = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(final JSONObject user, GraphResponse response) {
            if (response.getError() == null) {
                setResult(RESULT_OK);
            }
        }
    };


}

