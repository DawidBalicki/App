package com.wordpress.bennthomsen.bleuart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

public class Controller extends Application {

    public enum MessageFromDevice
    {
        ADMIN("ad"),
        USER("us"),
        SAVEDUSER("su"),
        ERROR("er"),
        WRONGPASSWORD("wp"),
        ACCESS1("1"),
        ACCESS2("2"),
        ACCESS12("12");

        private String message;

        MessageFromDevice(String messageFromDevice) {
            this.message = messageFromDevice;
        }

        public String getMessage() {
            return message;
        }

    }

    public static final int REQUEST_SELECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = "nRFUART";
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private static final int STATE_OFF = 10;
    private static int mState = UART_PROFILE_DISCONNECTED;
    private static BluetoothDevice mDevice = null;
    private static BluetoothAdapter mBtAdapter = null;
    private static UartService mService ;
    private static String dane;
    private static Boolean accessDoor1 = true;
    private static Boolean accessDoor2 = true;
    private static Boolean accessToNetwork = false;
    private static String name;

    protected static UartService getmService() {
        return mService;
    }
    protected static void setmService(UartService mService) {
        Controller.mService = mService;
    }
    protected static void setDane(String dane) {
        Controller.dane = dane;
    }
    protected static String getDane() {
        return dane;
    }
    protected static BluetoothAdapter getmBtAdapter() {
        return mBtAdapter;
    }
    protected static void setmBtAdapter(BluetoothAdapter mBtAdapter) {
        Controller.mBtAdapter = mBtAdapter;
    }
    protected static void setmDevice(BluetoothDevice mDevice) {
        Controller.mDevice = mDevice;
    }
    protected static BluetoothDevice getmDevice() {
        return mDevice;
    }
    protected static int getmState() {
        return mState;
    }
    protected static void setmState(int mState) {
        Controller.mState = mState;
    }
    protected static int getUartProfileConnected() {
        return UART_PROFILE_CONNECTED;
    }
    protected static void setAccessDoor1(Boolean accessDoor1) {
        Controller.accessDoor1 = accessDoor1;
    }
    protected static Boolean getAccessDoor1() {
        return accessDoor1;
    }
    protected static void setAccessDoor2(Boolean accessDoor2) {
        Controller.accessDoor2 = accessDoor2;
    }
    public static Boolean getAccessDoor2() {
        return accessDoor2;
    }
    public static void setAccessToNetwork(Boolean accessToNetwork) {
        Controller.accessToNetwork = accessToNetwork;
    }
    public static Boolean getAccessToNetwork() {
        return accessToNetwork;
    }
    public static String getName() {
        return name;
    }
    public static void setName(String name) {
        Controller.name = name;
    }

    public static ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = (((UartService.LocalBinder) rawBinder).getService());
            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                //finish();
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            mService.disconnect();
            mService = null;
        }
    };

    private void finish() {
    }

    public static final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            final Intent mIntent = intent;

            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "UART_CONNECT_MSG");
                        mState = UART_PROFILE_CONNECTED;
                    }
                });
            }

            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "UART_DISCONNECT_MSG");
                        mState = UART_PROFILE_DISCONNECTED;
                        mService.close();
                    }
                });
            }

            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.enableTXNotification();
            }

            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                try {
                    String text = new String(txValue, "UTF-8");
                    dane = text;
                    Log.d(TAG,"Data from Client: " + text);

                    for(MessageFromDevice za : MessageFromDevice.values())
                    {
                        if(text.equals(za.getMessage())){
                            Log.d(TAG,za.getMessage());
                            messageHandler(context, za);
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            setDane(text);
                            Log.d(TAG,"DANEEEEE: " +text);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }

            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
               // showMessage("Device doesn't support UART. Disconnecting");
                Log.d(TAG,"Device doesn't support UART. Disconnecting");
                getmService().disconnect();
            }
        }
    };

    public static void messageHandler(final Context context, MessageFromDevice message){

        switch(message){

            case ADMIN:
                Intent newIntent = new Intent(context,AdminActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntent);
                Toast.makeText(context,"Welcome", Toast.LENGTH_SHORT).show();
                break;

            case USER:
                Controller.setAccessToNetwork(true);
                break;

            case SAVEDUSER:
                Toast.makeText(context.getApplicationContext(),"Account has been created", Toast.LENGTH_SHORT).show();
                break;

            case ACCESS1:
                if(Controller.getAccessToNetwork()) {
                    Controller.setAccessDoor1(true);
                    Controller.setAccessDoor2(false);
                    Intent newIntent1 = new Intent(context, UserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newIntent1);
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACCESS2:
                if(Controller.getAccessToNetwork()) {
                    Controller.setAccessDoor1(false);
                    Controller.setAccessDoor2(true);
                    Intent newIntent2 = new Intent(context, UserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newIntent2);
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACCESS12:
                if(Controller.getAccessToNetwork()) {
                    Controller.setAccessDoor1(true);
                    Controller.setAccessDoor2(true);
                    Intent newIntent12 = new Intent(context, UserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newIntent12);
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
                }
                break;

            case ERROR:
                Toast.makeText(context.getApplicationContext(),"The account does not exist", Toast.LENGTH_SHORT).show();
                Intent newIntent123 = new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntent123);
                break;

            case WRONGPASSWORD:
                Toast.makeText(context.getApplicationContext(),"Wrong password. Try again...", Toast.LENGTH_SHORT).show();
                Intent newIntentWP = new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntentWP);
                break;
        }

    }
    private static void runOnUiThread(Runnable action) {

    }

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

}
