package com.wordpress.bennthomsen.bleuart;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import static android.content.ContentValues.TAG;

class Messages {

    Messages(){}

    void login(String login, String password){
        String message;
        String action;

        if(login.equals("admin")) action = "a";
        else action = "u";

        message = createMessage(action, "N", login, password, "N");
        Log.d(TAG,"Login:" + message);

        byte[] value;
        try {
            value = message.getBytes("UTF-8");
            Controller.getmService().writeRXCharacteristic(value);  //send data to service

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(login.equals("admin") && password.equals("admin")) Log.d(TAG,"Admin is logged.");
        else Log.d(TAG,"User:  " + message + "is logged.");
    }

    void createAccount(String login, String password, Boolean door1, Boolean door2){
        String accessDoor1 = "";
        String accessDoor2 = "";
        if(door1) accessDoor1 = "1";
        if (door2) accessDoor2 = "2";
        String accessToDoors = accessDoor1 + accessDoor2;

        String message = createMessage("s","N",login,password,accessToDoors);
        Log.d(TAG, "Create Account: " + message);

        byte[] value;
        try {
            value = message.getBytes("UTF-8");
            Controller.getmService().writeRXCharacteristic(value);  //send data to service

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    void closeDoor(String doorId){
        String message = createMessage("c",doorId);   //message example: close:1
        Log.d(TAG, "Close door: " + doorId + " : " + message);

        byte[] value;
        try {
            value = message.getBytes("UTF-8");
            Controller.getmService().writeRXCharacteristic(value);  //send data to service

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    void openDoor(String doorId){
        String message = createMessage("o",doorId);
        Log.d(TAG, "Open door: " + doorId + " : " + message);

        byte[] value;
        try {
            value = message.getBytes("UTF-8");
            Controller.getmService().writeRXCharacteristic(value);  //send data to service

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    void checkTheStateOfDoor(String doorId){
        String message = createMessage("h", doorId);   //message example: check:1
        Log.d(TAG, "Check the state of door: " + doorId);
        byte[] value;
        try {
            value = message.getBytes("UTF-8");
            Controller.getmService().writeRXCharacteristic(value);  //send data to service

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    void displayUsers(){
        String message = createMessage("d","N","N","N","N");
        Log.d(TAG, "Display users: " + message);

        byte[] value;
        try {
            value = message.getBytes("UTF-8");
            Controller.getmService().writeRXCharacteristic(value);  //send data to service

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private static String createMessage(String action, String doorID, String login, String password, String access){
        return action + ":" + doorID + ":" + login + ":" + password + ":" + access + ":";
    }

    private static String createMessage(String action, String doorID){
        return action + ":" + doorID + ":" + "N" + ":" + "N" + ":" + "12" + ":";
    }

}
