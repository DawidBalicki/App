package com.wordpress.bennthomsen.bleuart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    Messages messages = new Messages();
    Button buttonOpenDoor1;
    Button buttonOpenDoor2;
    Button buttonCloseDoor1;
    Button buttonCloseDoor2;
    Button buttonStateOfDoor1;
    Button buttonStateOfDoor2;
    TextView textViewStateOfDoor1;
    TextView textViewStateOfDoor2;
    TextView nameOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        buttonOpenDoor1 = findViewById(R.id.buttonOpenUser);
        buttonOpenDoor2 = findViewById(R.id.buttonOpen1User);
        buttonCloseDoor1 = findViewById(R.id.buttonCloseUser);
        buttonCloseDoor2 = findViewById(R.id.buttonClose1User);
        buttonStateOfDoor1 = findViewById(R.id.buttonStateOfDoorUser);
        buttonStateOfDoor2 = findViewById(R.id.buttonStateOfDoor1User);
        textViewStateOfDoor1 = findViewById(R.id.stateOfDoorUser);
        textViewStateOfDoor2 = findViewById(R.id.stateOfDoor1User);
        nameOfUser = findViewById(R.id.nameOfUser);

        nameOfUser.setText(Controller.getName());
        grayOutButtons(Controller.getAccessDoor1(), Controller.getAccessDoor2());

        if(!Controller.getStatusOfDoor1()) {
            buttonCloseDoor1.setEnabled(false);
            buttonCloseDoor1.setAlpha(.3f);
        }
        else{
            buttonOpenDoor1.setEnabled(false);
            buttonOpenDoor1.setAlpha(.3f);
        }
        if(!Controller.getStatusOfDoor2()) {
            buttonCloseDoor2.setEnabled(false);
            buttonCloseDoor2.setAlpha(.3f);
        }
        else{
            buttonOpenDoor2.setEnabled(false);
            buttonOpenDoor2.setAlpha(.3f);
        }

        buttonOpenDoor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.openDoor("1");
            }
        });
        buttonOpenDoor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.openDoor("2");
            }
        });
        buttonCloseDoor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.closeDoor("1");
            }
        });
        buttonCloseDoor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.closeDoor("2");
            }
        });
    }

    private void grayOutButtons(Boolean accessDoor1, Boolean accessDoor2){
        if(!accessDoor1){
            buttonStateOfDoor1.setEnabled(false);
            buttonStateOfDoor1.setAlpha(.3f);
            buttonCloseDoor1.setEnabled(false);
            buttonCloseDoor1.setAlpha(.3f);
            buttonOpenDoor1.setEnabled(false);
            buttonOpenDoor1.setAlpha(.3f);
            textViewStateOfDoor1.setAlpha(.3f);

        }
        if(!accessDoor2){
            buttonStateOfDoor2.setEnabled(false);
            buttonStateOfDoor2.setAlpha(.3f);
            buttonCloseDoor2.setEnabled(false);
            buttonCloseDoor2.setAlpha(.3f);
            buttonOpenDoor2.setEnabled(false);
            buttonOpenDoor2.setAlpha(.3f);
            textViewStateOfDoor2.setAlpha(.3f);
        }
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.popup_title_back)
                .setMessage(R.string.popup_message_back)
                .setPositiveButton(R.string.popup_yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent newIntent = new Intent(UserActivity.this, LoginActivity.class);
                        startActivity(newIntent);

                    }
                })
                .setNegativeButton(R.string.popup_no, null)
                .show();
    }
    }


