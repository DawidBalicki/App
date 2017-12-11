package com.wordpress.bennthomsen.bleuart;

import android.os.Binder;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

import static com.wordpress.bennthomsen.bleuart.MainActivity.TAG;

public class CreateAccount extends Activity {


    private Button saveAccountButton;
    private EditText loginForNewUser;
    private EditText passwordForNewUser;
    private CheckBox door1CheckBox;
    private CheckBox door2CheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        saveAccountButton = (Button) findViewById(R.id.saveAccountButton);
        loginForNewUser = (EditText) findViewById(R.id.loginForNewUser);
        passwordForNewUser = (EditText) findViewById(R.id.passwordForNewUser);
        door1CheckBox = (CheckBox) findViewById(R.id.checkBoxDoor1);
        door2CheckBox = (CheckBox) findViewById(R.id.checkBoxDoor2);
        saveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Log.d(TAG,"Login For New User: " + loginForNewUser.getText().toString());
              Log.d(TAG,"Password For New User: " + passwordForNewUser.getText().toString());
              Log.d(TAG,"Door1: " + door1CheckBox.isChecked());
              Log.d(TAG,"Door2: " + door2CheckBox.isChecked());







            }
        });



    }

}
