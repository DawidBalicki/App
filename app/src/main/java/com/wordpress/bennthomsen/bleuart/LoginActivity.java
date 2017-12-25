package com.wordpress.bennthomsen.bleuart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class LoginActivity extends Activity  {

    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Messages messages = new Messages();

    public View getmProgressView() {
        return mProgressView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                Log.d(TAG,"I am in LoginActivity (attemptLogin(): " + Controller.getDane());
            }
        });

    }

    private void attemptLogin() {
        mLoginView.setError(null);
        mPasswordView.setError(null);

        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        Controller.setName(login);

        Log.d(TAG,"Login: " + login);
        Log.d(TAG,"Password: " + password);

        boolean cancel = false;
        View focusView = null;

          if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isEmailValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_email));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            messages.login(login,password);

        }
    }

    private boolean isEmailValid(String login) {

        return login.length() > 4 && login.length() < 12 ;
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4 && password.length() < 12 ;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            mLoginFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
            showProgress(false);
            Controller.getmService().disconnect();
            Intent intent = new Intent(LoginActivity.this, ConnectWithDevice.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Your connection has been broken", Toast.LENGTH_SHORT).show();
        }
    }


